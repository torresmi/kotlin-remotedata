# RemoteData
Data type that represents the possible states when fetching data from a remote source. Kotlin port of https://github.com/krisajenkins/remotedata

This is a sealed class that provides 4 possible states when loading data. Similar to an Enum, but each case can have associated values. So you can always correctly model the state of the request operation at any given point.
 
 1. **NotAsked** - No operation in progress
 2. **Loading** - Operation in progress
 3. **Success** - Operation finished successfully, with the data
 4. **Failure** - Operation finished but failed, with the error 
  
| Failure Case | Success Case |
| --------------- | ---------------- |
| ![Failure](https://github.com/torresmi/kotlin-remotedata/raw/master/assets/error_case.gif) | ![Success](https://github.com/torresmi/kotlin-remotedata/raw/master/assets/success_case.gif)
 
 State when no operation was requested:
```kotlin
 var operation: RemoteData<MyError, MyData> = RemoteData.NotAsked
 ```
 State while loading:
 ```kotlin
  var operation: RemoteData<MyError, MyData> = RemoteData.Loading
 ```

 State after loading successfully:
 ```kotlin
 var operation: RemoteData<MyError, MyData> = RemoteData.Success(data)
 ```

 State after failure loading:
```kotlin
 var operation: RemoteData<MyError, MyData> = RemoteData.Failure(error)
 ```

Being a sealed class means that the compiler can help us think of all valid state possibilities, providing extra data when necessary.

```kotlin
fun render(operation: RemoteData<MyError, MyData>) {
  when (operation) {
    is RemoteData.NotAsked -> // show initial screen
    is RemoteData.Loading -> // show loading screen 
    is RemoteData.Success -> // show data using operation.data
    is RemoteData.Failure -> // show error screen using operation.failure
  }
}
```

There are helper functions to make working with all possibilities easier. 

```kotlin
// Get the value if finished loading, or use a default value. 
val item = operation.getOrElse(defaultItem)

// Map the value if it's a successful operation
operation.map { item -> /** update data */ }

// FlatMap with another RemoteData 
operation.flatMap { item -> /** bad data, return RemoteData.Failure */ } 

// Merge operations
operation.mergeWith(otherOperation) { item1, item2 -> /** combine results */ }
```

It's also useful in RxJava Observables to avoid keeping _known_ possible error states out of `onError`, leaving that terminal case for exceptional unexpected errors.

```kotlin
fun loadData(): Observable<RemoteData<MyError, Data>> {
  return fetchData()
    .map { response -> 
      if (response.code == 200) {
        RemoteData.succeed(response.data)
      else {
        RemoteData.fail(response.error)
      }
    }
    .startWith(RemoteData.Loading)
}
```

## Why 
We often need to fetch data, and doing so means that we often setup state for this operation. We might want to do the following: 

> Show a loading indicator until the data is fetched, and then show the data.

Sounds simple enough. A naive approach is the following:

State while loading:
```kotlin
 var isLoading: Boolean = true
 var item: Data? = null
```
State after loading:
```kotlin
 var isLoading: Boolean = false
 var item: Data? = loadedData
 ```
 But we forgot about handling errors. So we add error state to render an error screen if there is an error. 
 
 State while loading:
 ```kotlin
 var isLoading: Boolean = true
 var item: Data? = null
 var error: Throwable? = null
```

 State after loading successfully:
 ```kotlin
 var isLoading: Boolean = false
 var item: Data? = loadedData
 var error: Throwable? = null
 ```
 
 State after failure loading:
 ```kotlin
 var isLoading: Boolean = false
 var item: Data? = null
 var error: Throwable? = error
 ```
 
 There are issues with this approach. 
 1. We have to remember to reset the loading state if we successfully fetched data and if we failed to load data. It's easy to forget about reseting the loading flag if there was an error. 
 2. The state for data and the error are both now nullable, since they might not be available at all times of the operation.
 3. Chaining or merging operations can be difficult. For example, errors aren't propagated down the chain.
 3. We might have to duplicate this error prone state for other operations.
 4. This state allows for **16 different combinations**, and many of those wouldn't make sense. For example, if the state says it's loading but it also has an error or data. 
 
 If you have a list of items to load, one could use an empty list instead of `null`. However, there is still the issue of easily mixing up if the data is _truly_ empty or if it is just empty from loading or errors.
 ## Download
Gradle: 
```groovy
compile 'com.github.torresmi:remotedata:1.1'
```
Maven:
```xml
<dependency>
  <groupId>com.github.torresmi</groupId>
  <artifactId>remotedata</artifactId>
  <version>1.1</version>
  <type>pom</type>
</dependency>
```
