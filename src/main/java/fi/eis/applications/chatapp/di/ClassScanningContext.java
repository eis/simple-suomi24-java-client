package fi.eis.applications.chatapp.di;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Creation Date: 1.12.2014
 * Creation Time: 21:55
 *
 * @author eis
 */

/*
 * FileSystemBeanArchiveHandler:
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ClassScanningContext extends Context {
    boolean firstTime = true;
    private final Class sourceClass;

    public ClassScanningContext(Class sourceClass) {
        super();
        this.sourceClass = sourceClass;
    }

    public <T> T get(Class<T> type) {
        if (firstTime) {
            initClassList();
            firstTime = false;
        }
        return super.get(type);
    }
    private void initClassList() {
        // we really don't want to scan full class path so commented out...
        /*
        String classpath = System.getProperty("java.class.path");
        List<String> classpathEntries = Arrays.asList(classpath.split(File.pathSeparator));
        for(String entry: classpathEntries) {
            List<Class> classes = handle(entry).getClasses();
            super.modules.add(DependencyInjection.classes(classes));
        }
        */

        // a jar or a directory path of whoever initiated us
        URL currenturl = sourceClass.getProtectionDomain().getCodeSource().getLocation();
        List<Class> classes = handle(currenturl).getClasses();
        System.out.println("got classes " + classes);
        super.modules.add(DependencyInjection.classes(classes));
    }
    public static final String PROCOTOL_FILE = "file";
    public static final String PROCOTOL_JAR = "jar";
    public static final String PROCOTOL_HTTP = "http";
    public static final String PROCOTOL_HTTPS = "https";
    public static final String PROTOCOL_FILE_PART = PROCOTOL_FILE + ":";
    // according to JarURLConnection api doc, the separator is "!/"
    public static final String JAR_URL_SEPARATOR = "!/";

    static class BeanArchiveBuilder{
        private final List<Class> classes = new ArrayList<>();
        public void addClass(String className) {
            try {
                Class targetClass = Class.forName(className);
                if (targetClass.isInterface() || Modifier.isAbstract(targetClass.getModifiers())) {
                    return;
                }
                classes.add(targetClass);
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                System.out.println("Not found: " + className);
            }
        }
        public List<Class> getClasses() {
            return this.classes;
        }
    }

        public static final String CLASS_FILE_EXTENSION = ".class";

        public BeanArchiveBuilder handle(URL url) {
            return handle(new File(url.getPath()));
        }
        public BeanArchiveBuilder handle(String path) {

            return handle(new File(path));
        }
        public BeanArchiveBuilder handle(File file) {

            if(!file.exists()) {
                return null;
            }

            BeanArchiveBuilder builder = new BeanArchiveBuilder();

            try {
                log_debugv("Handle path: {0}", file.toPath());

                if (file.isDirectory()) {
                    handleDirectory(new DirectoryEntry().setFile(file), builder);
                } else {
                    handleFile(file, builder);
                }
            } catch (IOException e) {
                log_warn("Could not handle path: "+file.toPath() , e);
            }
            return builder;
        }

    private void log_warn(String s, Throwable t) {
        System.err.println(s);
        t.printStackTrace(System.err);
    }

    protected void handleFile(File file, BeanArchiveBuilder builder) throws IOException {

            log_debugv("Handle archive file: {0}", file);

            try {
                ZipFile zip = new ZipFile(file);
                Enumeration<? extends ZipEntry> entries = zip.entries();
                ZipFileEntry entry = new ZipFileEntry(PROCOTOL_JAR + ":" + file.toURI().toURL().toExternalForm() + "!/");
                while (entries.hasMoreElements()) {
                    add(entry.setName(entries.nextElement().getName()), builder);
                }
                zip.close();
            } catch (ZipException e) {
                throw new IllegalArgumentException(e);
            }
        }

        protected void handleDirectory(DirectoryEntry entry, BeanArchiveBuilder builder) throws IOException {

            log_debugv("Handle directory: {0}", entry.getFile());

            File[] files = entry.getFile().listFiles();

            if(files == null) {
                log_warnv("Unable to list directory files: {0}", entry.getFile());
            }
            String parentPath = entry.getName();

            for (File child : files) {

                if(entry.getName() != null ) {
                    entry.setPath(entry.getName() + "/" + child.getName());
                } else {
                    entry.setPath(child.getName());
                }
                entry.setFile(child);

                if (child.isDirectory()) {
                    handleDirectory(entry, builder);
                } else {
                    add(entry, builder);
                }
                entry.setPath(parentPath);
            }
        }

    private void log_warnv(String s, Object... args) {
        System.out.println(s + " - " +Arrays.asList(args));
    }

    private void log_debugv(String s, Object... args) {
        System.out.println(s + " - " +Arrays.asList(args));
    }

    protected void add(Entry entry, BeanArchiveBuilder builder) throws MalformedURLException {
            if (isClass(entry.getName())) {
                builder.addClass(filenameToClassname(entry.getName()));
            }
        }

        protected boolean isClass(String name) {
            return name.endsWith(CLASS_FILE_EXTENSION);
        }

        private String filenameToClassname(String filename) {
            return filename.substring(0, filename.lastIndexOf(CLASS_FILE_EXTENSION)).replace('/', '.').replace('\\', '.');
        }

        /**
         * An abstraction of a bean archive entry.
         */
        protected interface Entry {

            String getName();

            /**
             *
             * @return the URL, most probably lazily created
             * @throws MalformedURLException
             */
            URL getUrl() throws MalformedURLException;

        }

        private static class ZipFileEntry implements Entry {

            private String name;

            private String archiveUrl;

            ZipFileEntry(String archiveUrl) {
                this.archiveUrl = archiveUrl;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public URL getUrl() throws MalformedURLException {
                return new URL(archiveUrl + name);
            }

            ZipFileEntry setName(String name) {
                this.name = name;
                return this;
            }

        }

        private static class DirectoryEntry implements Entry {

            private String path;

            private File file;

            @Override
            public String getName() {
                return path;
            }

            @Override
            public URL getUrl() throws MalformedURLException {
                return file.toURI().toURL();
            }

            public DirectoryEntry setPath(String path) {
                this.path = path;
                return this;
            }

            public File getFile() {
                return file;
            }

            public DirectoryEntry setFile(File dir) {
                this.file = dir;
                return this;
            }

        }


    /*
    private void initClassList() {
        ClassLoader myCL = Thread.currentThread().getContextClassLoader();
        List<Class> classesList = new ArrayList<>();
        while (myCL != null) {
            System.out.println("ClassLoader: " + myCL);
            try {
                Vector<Class<?>> classVector = list(myCL);
                System.out.println("Found: " + classVector);

                // Point of loop below is that we want to skip interfaces and abstract
                // classes, as we can't instantiate them anyway to fulfill any dependencies
                // trying will cause a NPE

                Iterator<Class<?>> iterator = classVector.iterator();
                while(iterator.hasNext()) {
                    Class clazz = iterator.next();


                    if (clazz.isInterface()) {
                        continue;
                    }
                    if ( Modifier.isAbstract(clazz.getModifiers())) {
                        continue;
                    }
                    classesList.add(clazz);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
            myCL = myCL.getParent();
        }

        Class[] foundClasses = classesList.toArray(new Class[classesList.size()]);
        super.modules.add(DependencyInjection.classes(foundClasses));
    }
    private static Vector<Class<?>> list(ClassLoader classLoaderParam)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        Class classLoaderClass = classLoaderParam.getClass();
        while (classLoaderClass != java.lang.ClassLoader.class) {
            classLoaderClass = classLoaderClass.getSuperclass();
        }
        java.lang.reflect.Field classLoaderClassesField = classLoaderClass
                .getDeclaredField("classes");
        classLoaderClassesField.setAccessible(true);
        return (Vector<Class<?>>)classLoaderClassesField.get(classLoaderParam);
    }
    */
}
