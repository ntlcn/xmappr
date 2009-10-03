/*
 * This software is released under the BSD license. Full license available at http://www.xlite.org/license/
 *
 * Copyright (c) 2008, 2009, Peter Knego & Xlite contributors
 * All rights reserved.
 */
package org.xlite;

import java.io.StringReader;

public class ValueConverterWrapperTest {

    public static String xml = "" +
            "<root a=\"2.2\">\n" +
            "some text\n" +
            "<node>\n" +
            "123\n" +      // On purpose on it's own line
            "</node>\n" +
            "</root>";

    public static void main(String[] args) {

        StringReader reader = new StringReader(xml);

        Configuration conf = new Configuration(Root.class);
        Xlite xf = new Xlite(conf);
        Root root = (Root) xf.fromXML(reader);
    }

    @RootElement
    public static class Root {

        @Attribute
        public float a;

        @Element
        public Integer node;

        @Text
        public String text;
    }


}

