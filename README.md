# GyGReviewTest

This is the GYG Android Engineer Challenge

## Background

This app allows the user to see reviews of a Berlin tour. 
 
## Build

This project was build on Android Studio 3.1.3.

Download the project from the repository and import it into Android Studio and click on Run.
Disable Instant Run feature for this project.

In case your build the project with Andrid Studio 3.2 Beta, Instan run feature works.

## Project Structure and Why

The idea in this test was to include some of the Android Architecture Components.

Overview:

```
\---gygreviews
    \---api: Definition of the API (retrofit)
    \---db: Definition of my db (Room)
    \---model: Models for rest service and db (POJO classes)
    \---repository: Definition of my repository (fetchs data from rest service and db)
    \---util: Utils used by Google. Here is the wrapper for ApiResponse
    \---viewModel: Definition of the data exposed to the UI 
    \---vo: Value object suach as Resource and Status
```

One of the guidelines was:
"The app should function offline in any scenario (e.g. first time and returning user)"

So, the app should store data locally and fetchs data from the service. I focused on how to resolve with a roduction-quality architecture this scenario.
My solution is base on https://developer.android.com/jetpack/docs/guide using a repository component. It has a implemetation of `NetworkBoundResource`


### Issues I faced it

The giving webservice return 403 on mobile.

``
https://www.getyourguide.com/berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/reviews.json?count=5&page=0&rating=0&sortBy=date_of_review&direction=DESC
``

In order to consume the REST API, I have used Retrofit library. However for some reason I always get a 403 error.
I did't have enougt time for investigating the reason. I tried with other app and always got the 403.

Tested the app with :
- https://play.google.com/store/apps/details?id=org.mushare.httper
- https://play.google.com/store/apps/details?id=com.sn.restandroid
- my app.

However whe the client is the web browser it works. 

### Alternative path
So, I was blocked and with a few time. So, I decided to mock the webservice. 
In the real live this could be an issue and a blocker. However due to how I structure the project, it let me to mock the service.
With more time, I would create a much better architecture for mocks.

By this way, I 'fix' the data layer and I contiued with the app.


## Mock the API for submitting

Here I am going to describe how I woudl structure the POST payload and response.

First of all, the app should work on offline mode, so, this means I will store the request's payloads locally.
I have create a daba base using Room, the db contains a table called `NewReview`.

Every time the user tries to submitting a review, a new entry will be created. The idea is to have an observer on the table' data
and when there is a new entry (we might check internet connection before) we would try to make a POST.
If we have a success result, the data is removed from the table, if not, we left it there in case to retry again later (depending on the requirements).

The payload it's similar to review's structure:
``
@Entity
data class NewReview(@PrimaryKey val uiId: Int, val userId: Int, val rating: String, val title: String, val message: String, val foreignLanguage: Boolean, val date: String)
``
{
    uiId: Int,
    rating: String,
    title: String, 
    message: String,
    foreignLanguage: Boolean,
    date: String
}

Other attributes such as reviewerName, reviewerCountry, author are not necessary because we are provinding the user's id (assuming we are authenticated).

In case of a success response, it will be the review created, using the same review's payload that uses /reviews.json.

Example:

``
    {
      "review_id": 3107124,
      "rating": "4.0",
      "title": "Interessanter Blick auf den Tempelhofer Flughafen",
      "message": "Es wird vielgezeigt, die 2 Stunden sind schnell vorbei. Die Führung war etwas zu routiniert, weil zu oft abgehalten, aber recht informativ.",
      "author": "Monika – Germany",
      "foreignLanguage": true,
      "date": "July 21, 2018",
      "date_unformatted": {},
      "languageCode": "de",
      "traveler_type": "friends",
      "reviewerName": "Monika",
      "reviewerCountry": "Germany"
    }
``    

## What woudl I improve with more time?
- Figure out why the Service returns a 403.
- Build a much better mock service for testing. Right now `ReviewService` has the real and the mock method. 
- Include depending inyection. Right now the project has a interface called `ServiceLocator` that works as Service Locator.
- UI, even thoug it has a Material Desing theme, it is vary basic.
- Improve `ReviewsViewModel`. As I mentioned before I cound't play with the real rest service, so, I mocked the service using only `page` query. 
Right now everytime a new page is requested, it triggers a transformation that request new data from the repository.

``
    val list: LiveData<Resource<List<Review>>> = Transformations
            .switchMap(currentPage) { _ ->
                viewRepository.loadReviews(currentPage.value!!)
            }
``            
