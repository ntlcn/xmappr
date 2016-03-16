
# Using Xmappr #

## Reading XML ##

After mapping is defined (via annotations or XML config - see [quick start](QuickStart.md)), Xmappr can be invoked:

```
Xmappr xmapper = new Xmappr(MyClass.class);
MyClass myclass = (MyClass) xmapper.fromXML(reader);
```

where `MyClass` is your class for which you defined a mapping and `reader` is a `java.io.reader` where XML data is read from.

## Writing XML ##

Writing XML is done similarly: define mappings and then write your class to output.
```
Xmappr xm = new Xmappr(MyClass.class);
MyClass myclass = new ...
xmapper.toXML(myclass, writer)
```
Where `writer` is an instance of `java.io.Writer`.

## Multi-mapping ##

When you have as input different XML documents, that come in unspecified order, then with usual XML parsers you need to check the root element and decide which parsing path you are going to take.

With Xmappr you can simply declare multiple mappings. The correct mapping will then be automatically used based on the root element of given XML document. An example would be receiving XML documents from network (protocol implementation):

```
<packetone>
  <payload>something</payload>
</packetone>
```

should map to

```
@RootElement
class PacketOne{
  @Element
  String payload;
}
```

The other XML document is

```
<packettwo>
  <data>1234</data>
</packettwo>
```

and maps to

```
@RootElement
class PacketTwo{
  @Element
  byte[] data;
}
```

Using this mappings would then go like this:

```
// use the first mapping
Xmappr xmappr = new Xmappr(PacketOne.class);
// use the second mapping
xmappr.addMapping(PacketTwo.class);

// reading XML - the right mapping is automatically chosen based on XML received
Object obj = xmappr.fromXml(reader);

if(obj instanceof PacketOne){
  // do your thing..
} else if (obj instanceof PacketTwo){
  // do other thing..
}
```

`Xmappr.fromXml(reader)` will produce the correct object based on the XML data from `reader`.

## Mapping to an existing object instance ##

So far we have seen that on every invocation of {{{Xmappr.fromXml(reader)}} a new instance of an object was created. Sometimes you just want to map XML to an existing object instance:

```
xmappr.fromXml(reader, targetObject)
```

Here `targetObject` would be populated with values from XML read from `reader`.

## Pretty printing the output ##

Sometimes the output is viewed by humans an in this case you'd want it to be pretty printed:

```
xmapper.setPrettyPrint(true);
```

This will produce human readable XML with every XML element on it's own indented line.
**NOTE:** Pretty printing is processor intensive so writing XML will be several times slower and also produced XML will be bigger.

## Using custom converters ##
Xmappr has built-in converters for most basic Java types. In case a target type is not supported, a custom converter can be written and injected into Xmappr. This is described in detail in section on [converters](Converters.md).

## Handling namespaces ##
If your XML defines namespaces on a root element, then you can configure namespace mapping on Xmapper instance:

```
xmappr.addNamespace("prefix", "namespace");
```

More about it in a section on [namespaces](NameSpaces.md).