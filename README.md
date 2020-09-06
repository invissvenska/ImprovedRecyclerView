# ImprovedRecyclerView
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16) [![](https://jitpack.io/v/invissvenska/ImprovedRecyclerView.svg)](https://jitpack.io/#invissvenska/ImprovedRecyclerView) <span class="badge-buymeacoffee"><a href="https://www.paypal.com/paypalme/svenvandentweel/3" title="Donate to this project using Buy Me A Coffee"><img src="https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg" alt="Buy Me A Coffee donate button" /></a></span>  

Based on the deprecated [infinum MjolnirRecyclerView](https://github.com/infinum/MjolnirRecyclerView) library, but using AndroidX and more options: TBD.

## Prerequisites

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

## Dependency

Add this to your module's `build.gradle` file (make sure the version matches the JitPack badge above):

```gradle
dependencies {
    ...
    implementation 'com.github.invissvenska:ImprovedRecyclerView:VERSION'
}
```

## Configuration
ImprovedRecyclerView provides methods to add header, footers, empty view and more in a simple way by extending the default RecyclerView behaviour.

### Header & Footer
You can add optional header and footer to your list. These are optional so you don't have to set a header and/or footer.  
```java
adapter.setHeader(View view);
adapter.setFooter(View view);
```
  
### Empty view
Empty view is supported. It will be shown automatically when the adapter is empty, and automatically hides when the adapter is populated.  
```java
recyclerView.setEmptyView(View view);
```  
  
It is also possible to show the empty view while the adapter is not set. This is a handy when the adapter is initialized at a later point in time.  
```java
// show empty view when adapter is not set
recyclerView.setEmptyView(View view, true);
```

### onClick
The onClick methods are already implemented for you by the library.  
  
Implement the `OnClickListener<E>` interface in your Activity or Fragment and override the `onClick` method.  
```java
public class SimpleFragment extends Fragment implements SimpleAdapter.OnClickListener<String> {

    // some other code

   @Override
   public void onClick(int index, String item) {
       Toast.makeText(this.getContext(), item, Toast.LENGTH_SHORT).show();
   }
}
```

### Paging
Paging can be used if you want to use less data in your app. Just add the initial set of items, and when you scroll down the next set of items will be added to the list.  
  
Implement the `OnNextPageListener` interface in your Activity or Fragment and override the `onScrolledToNextPage` method.
```java
public class NextPageFragment extends Fragment implements SimpleAdapter.OnNextPageListener {

    // some other code

    @Override
    public void onScrolledToNextPage() {
        // Fetch data and add it to the list, show some loading widget in UI to indicate loading
        adapter.addAll(ITEMS);
    }
}
```

### Grid layout
Just use a GridLayoutManager instead of a LinearLayoutManager. If you want to use header and footer in your layout you can use the `setSpanSizeLookup` method set the full with of the RecyclerView to the header and footer.  
```java
...
final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
    @Override
    public int getSpanSize(int position) {
        switch(adapter.getItemViewType(position)) {
            case SimpleAdapter.TYPE_HEADER:
            case SimpleAdapter.TYPE_FOOTER:
                return layoutManager.getSpanCount();
            case SimpleAdapter.TYPE_ITEM:
            default:
                return 1;
        }
    }
});
recyclerView.setLayoutManager(layoutManager);

adapter = new SimpleAdapter(getContext());
recyclerView.setAdapter(adapter);

adapter.setHeader(R.layout.item_header);
adapter.setFooter(R.layout.item_footer);
...
```

## Screenshots

**Please click the image below to enlarge.**

<img src="https://raw.githubusercontent.com/invissvenska/ImprovedRecyclerView/master/media/collage.png">
