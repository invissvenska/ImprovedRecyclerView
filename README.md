# ImprovedRecyclerView
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19) [![](https://jitpack.io/v/invissvenska/ImprovedRecyclerView.svg)](https://jitpack.io/#invissvenska/ImprovedRecyclerView) <span class="badge-buymeacoffee"><a href="https://www.paypal.com/paypalme/svenvandentweel/3" title="Donate to this project using Buy Me A Coffee"><img src="https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg" alt="Buy Me A Coffee donate button" /></a></span>  

Based on the deprecated [infinum MjolnirRecyclerView](https://github.com/infinum/MjolnirRecyclerView) library, updated by using AndroidX and added more options: Parallax header and more is coming.

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
Just use a GridLayoutManager instead of a LinearLayoutManager. Size of header and footer will be automatically spanned across the grid spansize. 
```java
...
recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

adapter = new SimpleAdapter(getContext());
recyclerView.setAdapter(adapter);

adapter.setHeader(R.layout.item_header);
adapter.setFooter(R.layout.item_footer);
...
```

### DiffUtil
Only updates the different values in the RecyclerView and let the same values intact when giving a new list of items.  

Extend the `DiffUtil.Callback` class and update the `getChangePayload` method according your needs. In this example only a string value is added to the Bundle.
```java
public class ItemsDiffUtil extends DiffUtil.Callback {
    public static final String EXTRA_ITEM_DESCRIPTION = "EXTRA_ITEM_DESCRIPTION";

    private List<Item> oldList;
    private List<Item> newList;

    public ItemsDiffUtil(List<E> oldList, List<E> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ITEM_DESCRIPTION, newList.get(newItemPosition).getName());
        return bundle;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName());
    }
}
``` 
  
In your Adapter add the following lines to in the `onBind` method to update only fill the updated items.
```java
@Override
protected void bind(final Item item, final int position, List<Object> payloads) {
    if (payloads.isEmpty()) {
        tvPosition.setText(String.valueOf(item.getId()));
        tvText.setText(item.getName());
    } else {
        Bundle bundle = (Bundle) payloads.get(0);
        tvText.setText(bundle.getString(ItemsDiffUtil.EXTRA_ITEM_DESCRIPTION));
    }
    ...
}
```

In your Fragment or Activity call the `update` method in you Adapter to update only the different items:
```java
adapter.update(newList, new ItemsDiffUtil(items, newList));
```

### Parallax header
When you want to feel more depth in you app you can use the parallax option for the header.  

Add the height of your header layout to your adapter, and add the `ImprovedHeaderScrollListener` to your RecyclerView.
```java
LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
recyclerView.setLayoutManager(layoutManager);
recyclerView.setEmptyView(emptyView);

adapter = new SimpleAdapter(getContext(), (int) getResources().getDimension(R.dimen.parallax_height));
adapter.setHeader(R.layout.item_header);

recyclerView.setAdapter(adapter);
recyclerView.addOnScrollListener(new ResizeScrollListener<>(adapter, layoutManager));
adapter.addAll(ITEMS);
```

## Screenshots

**Please click the image below to enlarge.**

<img src="https://raw.githubusercontent.com/invissvenska/ImprovedRecyclerView/master/media/collage.png">
