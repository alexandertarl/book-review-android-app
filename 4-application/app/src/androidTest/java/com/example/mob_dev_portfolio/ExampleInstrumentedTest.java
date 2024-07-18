package com.example.mob_dev_portfolio;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.mob_dev_portfolio.activities.BookReviewsActivity.db;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import com.example.mob_dev_portfolio.activities.BookReviewsActivity;
import com.example.mob_dev_portfolio.activities.BookSearchActivity;
import com.example.mob_dev_portfolio.daos.BookReviewDao;
import com.example.mob_dev_portfolio.databases.BookReview;
import com.example.mob_dev_portfolio.databases.BookReviewDatabase;

import java.util.concurrent.TimeUnit;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    String typedString;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.mob_dev_portfolio", appContext.getPackageName());
    }

    // code to setup the database inside the tests class
    // taken from Developers Android Documentation
    // accessed 11-05-2023
    // https://developer.android.com/training/data-storage/room/testing-db
    private BookReviewDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, BookReviewDatabase.class).build();
    }
    // end of referenced code

    @Test
    public void inputReview() {
        // sets data
        String name = "name";
        String author = "author";
        Boolean like = true;
        String date = "2023-05-10";
        String comment = "comment";

        // insert into database
        BookReviewDao dao = db.bookReviewDao();
        BookReview br = new BookReview();
        br.setReviewId(0);
        br.setBookName(name);
        br.setBookAuthor(author);
        br.setLike(like);
        br.setDate(date);
        br.setComment(comment);
        dao.insertAll(br);

        // assert that has been inputted to database
        assertEquals(br.getBookName(), name);
        assertEquals(br.getBookAuthor(), author);
        assertEquals(br.getDate(), date);
        assertEquals(br.getComment(), comment);
    }

    // espresso tests below
    @Rule
    public ActivityScenarioRule<BookSearchActivity> searchActivityScenarioRule = new ActivityScenarioRule<BookSearchActivity>(BookSearchActivity.class);

    // code to avoid error when running espresso tests
    // taken from post by Mr-IDE 15-01-2019
    // accessed 11-05-2023
    // https://stackoverflow.com/questions/39457305/android-testing-waited-for-the-root-of-the-view-hierarchy-to-have-window-focus
    @Before
    public void setContextForEspresso() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
    // end of referenced code

    @Test
    public void buttonOnSearchFragmentTest() {
        onView(withId(R.id.tbrButtonAll)).check(matches(isDisplayed()));
    }

    // makes sure button1 is not displayed yet, as search not entered
    @Test
    public void tbrButton1OnSearchFragmentTest() {
        onView(withId(R.id.tbrButton1)).check(matches(not(isDisplayed())));
    }

    // makes sure test view is displayed but empty string
    @Test
    public void firstResultsEmptyBeforeSearchTest() {
        onView(withId(R.id.name1)).check(matches(isDisplayed()));
    }

    @Before
    public void initValidString() {
        typedString = "Misery";
    }

    @Test
    public void changeTextInFragment() throws InterruptedException {
        // search typed string and press enter
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.search)).perform(typeText(typedString));
        onView(withId(R.id.search)).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // has to wait a few seconds, so has time to load search results to textview
        TimeUnit.SECONDS.sleep(5);

        // check textview changed
        onView(withId(R.id.name1))
                .check(matches(withText(containsString(typedString))));
    }
}