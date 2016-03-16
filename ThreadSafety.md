# Thread safety #

Xmappr is thread safe and can be used in multiple threads concurrently. Let’s take a look at typical Xmappr use to see how this is achieved:
```
// configuring Xmappr to map to Root class
Xmappr xmappr = new Xmappr(Root.class);

// After configuration xmapper can be called multiple times with different input
Root root = (Root) xmappr.fromXML(reader);
// This can safely be called concurrently from another thread
Root root2 = (Root) xmappr.fromXML(reader2);
```
As you can see using Xmappr is done in two parts:

  1. Configuring Xmappr
  1. Invoking the mapping (`toXml(), fromXML()`..)

Xmappr configuration is computing intensive so it should be done only once. After class Xmappr is initialized, it's configuration can not be changed any more and it will throw an exception if you try to do so. Effectively class Xmappr becomes immutable after it was used for the first time. This makes it thread safe.

After it’s configured, Xmappr is ready to be used. Mapping is performed by calling `toXML()` or `fromXML()` on Xmappr instance. Xmappr itself is an immutable class – after it is initialized it’s internal state does not change. Being immutable makes it thread safe.

In order for  Xmappr to be thread safe also all converters must be thread safe. To achieve this they adhere to one of the two rules:

  1. Converters must be immutable, which means that they have no internal state or their internal state is also immutable (doesn’t change after initialization).
  1. If they must have internal state which is mutable they should initialize it on first use and save it to `ThreadLocal`.

Most converters adhere to the first point. An exception is `DateConverter` which adheres to the second. Internally it uses `java.text.DateFormat`, which is slow to initialize (so we don’t want to do it on every invocation) and is also not thread safe. `DateFormat` is initialized on first use and saved to `ThreadLocal` inside `DateConverter`.