**Mobile Development 2022/23 Portfolio**
# Requirements

Student ID: 21061629

_Complete the information above and then enumerate your functional and non functional requirements below._

## Functional Requirements

- The user can navigate to different fragments using the bottom navigation bar and toolbar
- Pressing an option on the bottom navigation bar takes the user to the correct activity
- There are sections in the application for: searching books, my reviews, creating a review, and liked book reviews
- The user receives a popup (toast) when they have entered a book review that is valid
- If the user does not input a book name for a review, they cannot input the review and are given a popup saying the review has not been submitted
- The user can click a like button if they especially liked the book they are reviewing, and this book is outputted to the liked book fragment
- Clicking on a book title takes the user to the review they wrote for it
- A room database is used to store the book reviews of the user, so they can be outputted in the application
- The user can search for book titles (using the Google Books API) so that they can find new books to read
- Books can be reviewed by entering details for: book name, a star rating, like/favourite button, date completed, more comments section
- When a user presses enter after searching for a book title, results are shown
- The search results for a book display: book title, author, book cover, and the Google Books link, for each of the 3 results
- Pressing the 'add to tbr' button creates a toast to tell the user the book has been added to their to-be-read list
- Pressing the 'get list of tbr books' button sends a notification to the user, of the list of books they added
- Clicking the box to enter the day a book was completed brings up a date picker 
- The user can only submit a book rating between the values of 0 and 10




## Non-Functional Requirements

- A consistent font is used throughout the application
- A consistent colour scheme is used throughout the application
- Each page has some text explaining what to expect on the page
- Searching for a book (using the API) in the search bar returns results within 5 seconds after the JSON response is recieved
- When a user clicks a Google Books link, the external intent should load within 5 seconds
- Navigating using the bottom navigation bar takes less than 5 seconds to load the new fragment after pressing a button
- Navigating to the search activity using the toolbar takes less than 5 seconds to load

