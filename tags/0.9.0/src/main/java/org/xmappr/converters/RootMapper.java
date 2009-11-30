/*
 * This software is released under the BSD license. Full license available at http://xmappr.googlecode.com
 *
 * Copyright (c) 2008, 2009, Peter Knego & Xmappr contributors
 * All rights reserved.
 */
package org.xmappr.converters;

import org.xmappr.MappingContext;
import org.xmappr.XMLSimpleReader;
import org.xmappr.XMLSimpleWriter;
import org.xmappr.XmapprException;

import javax.xml.namespace.QName;

/**
 * RootMapper sits at the top of the Mapper hierarchy
 *
 * @author peter
 */
public class RootMapper {

    private QName rootNodeName;
    private MappingContext mappingContext;
    private ElementConverter elementConverter;
    private Class rootClass;
    private ElementConverter converter;

    public RootMapper(QName rootNodeName, Class rootClass, ElementConverter elementConverter, MappingContext mappingContext) {
        this.rootClass = rootClass;
        this.elementConverter = elementConverter;
        this.rootNodeName = rootNodeName;
        this.mappingContext = mappingContext;
    }

    public QName getRootNodeName() {
        return rootNodeName;
    }

    public Class getRootClass() {
        return rootClass;
    }

    public Object getRootObject(XMLSimpleReader reader) {
      return getRootObject(reader, null);
    }

    public Object getRootObject(XMLSimpleReader reader, Object targetObject) {
        QName firstElement = reader.getRootName();
        if (firstElement.equals(rootNodeName)) {
            reader.moveDown();
            return elementConverter.fromElement(reader, mappingContext, "", null, rootClass, targetObject);
        } else {
            throw new XmapprException("Error: wrong XML element name. Was expecting root element <" + rootNodeName +
                    "> in input stream, but instead got <" + firstElement + ">.");
        }
    }

    public void toXML(Object object, XMLSimpleWriter writer) {
        writer.predefineNamespaces(mappingContext.getPredefinedNamespaces());
        elementConverter.toElement(object, rootNodeName, writer, mappingContext, "", null);
        writer.endDocument();
    }

    public ElementConverter getConverter() {
        return converter;
    }
}