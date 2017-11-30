GenericRenderers [![Build Status](https://travis-ci.org/Shyish/GenericRenderers.svg?branch=master)](https://travis-ci.org/Shyish/GenericRenderers) [![JitPack](https://jitpack.io/v/Shyish/GenericRenderers.svg)](https://jitpack.io/#Shyish/GenericRenderers) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-GenericRenderers-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3364)
===

Based on [Renderers lib](https://github.com/pedrovgs/Renderers) made by [pedrovgs](https://github.com/pedrovgs)


DIFFERENCE WITH RENDERERS
---

The main difference of this project is that is totally generic, which means:

* No need to wrap every model in another object.
* Possibility to bind more complex objects without extra effort.

But be aware that also means that **you'll loose type safety**.

USAGE
---

First of all, let's create as many Renderers as different views we need, for example:

```java
public class VideoRenderer extends Renderer<Video> {

    @Bind(R.id.iv_thumbnail)
    ImageView thumbnail;
    @Bind(R.id.tv_title)
    TextView title;

    /**
     * Inflate the main layout used to render videos in the list view.
     *
     * @param inflater LayoutInflater service to inflate.
     * @param parent ViewGroup used to inflate xml.
     * @return view inflated.
     */
    @Override
    protected View inflate(LayoutInflater inflater, ViewGroup parent) {
        View inflatedView = inflater.inflate(R.layout.video_renderer, parent, false);
        /*
         * You don't have to use ButterKnife library to implement the mapping between your layout
         * and your widgets you can implement setUpView and hookListener methods declared in
         * Renderer<T> class.
         */
        ButterKnife.bind(this, inflatedView);
        return inflatedView;
    }

    @OnClick(R.id.iv_thumbnail)
    void onVideoClicked() {
        Video video = getContent();
        Toast.makeText(getContext(), "Video clicked. Title = " + video.getTitle(), Toast.LENGTH_LONG).show();
    }

    /**
     * Main render algorithm based on render the video thumbnail, render the title, render the marker
     * and the label.
     */
    @Override
    public void render(List<Object> payloads) {
        Video video = getContent();
        Picasso.with(getContext()).cancelRequest(thumbnail);
        Picasso.with(getContext())
              .load(video.getThumbnail())
              .placeholder(R.drawable.placeholder)
              .into(thumbnail);
        title.setText(video.getTitle());
    }
}
```

Now there are 3 possible usages:

### Basic usage: only 1 model

```java
RendererBuilder<Video> rendererBuilder = new RendererBuilder<>(new VideoRenderer());
recyclerView.setAdapter(new RendererAdapter<>(rendererBuilder, videoCollection));
```

![That's it!](./art/screenshot_demo_1.jpg?raw=true)

### Advanced usage: multiple models

Ok, let's asume we now have another Renderer called `SectionRenderer` which is basically a section separator for our 
videos. Since is a simple header we just want to bind it with a String object, like:

```java
RendererBuilder rendererBuilder = new RendererBuilder()
              .bind(Video.class, new VideoRenderer())
              .bind(String.class, new SectionRenderer());
```

As you can see we use the default constructor for the `RendererBuilder` and use the chained bind methods. Now we set the 
adapter:

```java
RendererAdapter adapter = new RendererAdapter(rendererBuilder);
recyclerView.setAdapter(adapter);
```

We don't provide our list in the constructor anymore, since we want to add the headers dynamically, like:

```java
for (int i = 0, videoCollectionSize = videoCollection.size(); i < videoCollectionSize; i++) {
    adapter.add("Video #" + (i + 1));
    adapter.add(videoCollection.get(i));
}
```

As you can see there's no problem in adding different types since the list in the adapter will be of type `Object`. In 
case that you add a different type that doesn't have a Renderer associated with, an exception will be thrown.

![Result](./art/screenshot_demo_2.jpg?raw=true)

### More complex usage: multiple complex models

Ok, let's go for a bit more complex thing, let's imagine that now I want to add a single footer at the end of the list 
with the `FooterRenderer` that you can see in the example. The type will be again a String class, so we need to 
differentiate between the String associated with the `SectionRenderer`, like this:

```java
RendererBuilder rendererBuilder = new RendererBuilder()
              .bind(Video.class, new VideoRenderer())
              .bind(TYPE_FOOTER, new FooterRenderer())
              .bind(TYPE_SECTION, new SectionRenderer());
```

Where those types are just integers.
Finally we do the same as we did before but we add our footer at the end with one exception:

```java
RendererAdapter adapter = new RendererAdapter(rendererBuilder);
recyclerView.setAdapter(adapter);
for (int i = 0, videoCollectionSize = videoCollection.size(); i < videoCollectionSize; i++) {
    adapter.add(new RendererContent<>("Video #" + (i + 1), TYPE_SECTION));
    adapter.add(videoCollection.get(i));
}
adapter.add(new RendererContent<>("by Alberto Ballano", TYPE_FOOTER));
```

As you see we need to add the wrapper now, since we need a generic object in which put the TYPE. But as you can see 
that's only for the objects that have to be mapped this way, so the Video class stays the same, no wrapper at all!

Of course we also need to modify the `SectionRenderer` to use a different type:

```java
public class SectionRenderer extends Renderer<RendererContent<String>>
```

![Beautiful!](./art/screenshot_demo_3.jpg?raw=true)

INCLUDING IN YOUR PROJECT
---

With gradle: edit your build.gradle
```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

dependencies {
        compile 'com.github.Shyish:GenericRenderers:1.0'
}
```

Or declare it into your pom.xml

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Shyish</groupId>
    <artifactId>GenericRenderers</artifactId>
    <version>1.0</version>
</dependency>
```

UPDATES
---
* v1.0 Base version.
