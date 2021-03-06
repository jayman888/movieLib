/**
 * Student ID: 18347500
 *       Name: Jared Steiner
 *     Campus: Parramatta
 * Tutor Name: Jordan Collier
 *  Class Day: Tuesday
 * Class Time: 9am
 */

/*
TO DO SYNTAX:
(Using '0' instead of 'O' so they don't appear in To Do list)
Current Tasks (List of To Do)
  //T0D0: [[OrderNum][SubOrderNum]] Task Details
In Progress Tasks (Currently working on)
  //N0TE: [[OrderNum][SubOrderNum]] Task Details
Completed Taks
  //D0NE: [[OrderNum][SubOrderNum]] Task Details [Completion Notes]
*/
import java.util.Comparator;

public class Movie_18347500 {

    int movieID;             // Unique ID for each movie
    String title;            // Name of movie
    String director;         // Name of director
    String writer;           // Name of writer
    float duration;          // duration in hours e.g. 2.5 hours
    String genre;            // Type of movie
    String classification;   // Classification of movie content
    String releaseDate;      // Date in MONTH YYYY format
    float rating;            // Whole or half numebr for rating, e.g 1,2,4.5,2.5 NOT 1.9, 2.6

    public Movie_18347500 (int idNum, String movieName, String directorName, String writerName, float movieDuration, String movieGenre, String movieClassification, String movieReleaseDate, float movieRating) {
      // Maybe have a check to see if any crucial data is blank?
      movieID = idNum;
      title = movieName;
      director = directorName;
      writer = writerName;
      duration = movieDuration;
      genre = movieGenre;
      classification = movieClassification;
      releaseDate = movieReleaseDate;
      rating = movieRating;
    }

    public Movie_18347500 () {
      movieID = -1;
      title = "";
      genre = "";
    }

    public Movie_18347500(String[] inputList, Boolean hasRating) {

      movieID = Integer.parseInt(inputList[0]);
      title = inputList[1];
      director = inputList[2];
      writer = inputList[3];
      duration = Float.parseFloat(inputList[4]);
      genre = inputList[5];
      classification = inputList[6];
      releaseDate = inputList[7];
      if (hasRating) {
        rating = Float.parseFloat(inputList[8]);
      }
      else {
        rating = (float)(-1);
      }
    }

    // Uses Comparator to sort through Movie Ojbects by name Attribute
    public static Comparator<Movie_18347500> COMPARE_BY_NAME = new Comparator<Movie_18347500>() {
      public int compare(Movie_18347500 movieA, Movie_18347500 movieB) {
        return movieA.getMovieName().compareTo(movieB.getMovieName());
      }
    };

    // Uses Comparator to sort through Movie Objects by genre Attribute
    public static Comparator<Movie_18347500> COMPARE_BY_GENRE = new Comparator<Movie_18347500>() {
        public int compare(Movie_18347500 movieA, Movie_18347500 movieB) {
            return movieA.getMovieGenre().compareTo(movieB.getMovieGenre());
        }
    };

    // DONE: [10] Movie Getters

    // ## GETTERS ##
    // TIP: Returns values from object. Check constructors for variable specifics

    // MovieID Getter
    public int getMovieID() {
      return movieID;
    }

    // Movie Title Getter
    public String getMovieName() {
      return title;
    }

    // Movie Director Getter
    public String getMovieDirector() {
      return director;
    }

    // Movie Writer Getter
    public String getMovieWriter() {
      return writer;
    }

    // Movie Duration Getter
    public float getMovieDuration() {
      return duration;
    }

    // Movie Genre Getter
    public String getMovieGenre() {
      return genre;
    }

    // Movie Classification Getter
    public String getMovieClassification() {
      return classification;
    }

    // Movie Release Date Getter
    public String getMovieRelease() {
      return releaseDate;
    }

    // Movie Rating Getter
    public float getMovieRating() {
      return rating;
    }

    // DONE: [12] Movie Setters (only Genre and Rating)

    // Movie Genre Setter
    // Expects input to be validated against list of approved genres
    public void setMovieGenre(String newGenre) {
      genre = newGenre;
    }

    // Movie Rating Setter
    // Expects input to be validatesd to approriate rating
    public void setMovieRating(float newRating) {
      rating = newRating;
    }

}
