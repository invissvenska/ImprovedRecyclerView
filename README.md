# ImprovedRecyclerView
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16) [![](https://jitpack.io/v/invissvenska/ImprovedRecyclerView.svg)](https://jitpack.io/#invissvenska/ImprovedRecyclerView) <span class="badge-buymeacoffee"><a href="https://www.paypal.com/paypalme/svenvandentweel/3" title="Donate to this project using Buy Me A Coffee"><img src="https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg" alt="Buy Me A Coffee donate button" /></a></span>  

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
  
Implement the `OnClickListener<E>` in your Activity or Fragment and override the `onClick` method.  
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
  
Implement the `OnNextPageListener` in your Activity or Fragment and override the `onScrolledToNextPage` method.
```java
public class NextPageFragment extends Fragment implements SimpleAdapter.OnNextPageListener {

    // some other code

    @Override
    public void onScrolledToNextPage() {
        // Simulate network call by showing progressbar and adding data to adapter with some delay.
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isRemoving()) {
                    adapter.addAll(ITEMS);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, 3000);
    }
}
```

## Screenshots

**Please click the image below to enlarge.**

<img src="https://raw.githubusercontent.com/invissvenska/ImprovedRecyclerView/master/media/collage.png">
