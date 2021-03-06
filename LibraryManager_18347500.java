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

import java.io.*;
import java.util.Scanner;
import java.util.Objects;
import java.util.Comparator;
import java.util.Arrays;

public class LibraryManager_18347500 {

  // Constants
  public static final int LIB_SIZE = 150;
  public static final int EXPAND_SIZE = 10;

  // File Names
  public static String movieFileName = "movieLibrary.txt";
  public static String playlistFileName = "playlists.txt";

  // Global Keyboard Instance
  public static Scanner kb = new Scanner(System.in);
  public static Movie_18347500[] movieLibrary = initialiseMovieList(movieFileName);                    // Initialises Movie Library to List
  public static Playlist_18347500[] playlists = initialisePlaylistList(playlistFileName);              // Initialises Playlists to List

  public static void main(String[] args) throws IOException{

    System.out.println("\n ---- IMPORTED PROGRAM FILES ---- ");

    rootMenu(movieLibrary, playlists);

    kb.close();
  }


  /**
   * Creates a file object based of given filename
   *
   * @param  fileName    name of the file on the computer e.g. myFile.txt
   * @param  doValidate  whether to persist for file name if doesn't exist (playlists.txt doesn't have to exist)
   * @return             File instance pointing towards fileName
   */
  static File createFileInst(String fileName, Boolean doValidate) {
    File inFile = new File(fileName);           // Existing filename e.g. Book.txt or directory e.g. Texts/Book.txt
    // Requests new file name if not already
    if (doValidate) {                           // Whether it will repeat until pre-existing file found
      while (!inFile.exists()) {                // FILENAME SEARCH PERSIST
        System.out.println("ERROR: '" + fileName + "' not found!");
        System.out.print("Enter file location: ./");
        inFile = new File(kb.next());
      }
      System.out.println(" >> " + fileName + " successfully found and imported !");
    }
    else {
      if (!inFile.exists()) {                   // FILENAME SEARCH NO-PERSIST. CREATE BLANK FILE FOR USE
        System.out.println("ERROR: '" + fileName + "' not found!");
        System.out.println("Blank file created. If you have a pre-existing Playlists file please restart the program after renaming it 'playlists.txt'.");
      }
      else {                                    // FILE FOUND ON FIRST TRY WITH NO PERSISTANCE. WELL DONE
        System.out.println(" >> " + fileName + " successfully found and imported !");
      }
    }

    return inFile;
  }

  /**
   * Populates an array with movie information for movieLibrary to adopt
   *
   * @param fileName  name of the file on the computer to read from e.g. myFile.txt
   * @return          Array of movie objects to become the movie library
   */
  static Movie_18347500[] initialiseMovieList(String fileName) {
    Movie_18347500[] outputArray = new Movie_18347500[LIB_SIZE];				  // Creating the initial movie Library
    int arrayCounter = 0;														  // Keeps track of next available index in outputArray

    File userInputFile = createFileInst(fileName, true);                          // Create File Instance based off user input
    Scanner fileScanner = new Scanner(System.in);                                 // Creating keyboard input first allows try catch for FNF and also 'may not be initialised issue'
    // CANT THROW IOException OUTSIDE OF METHOD, DEFINING FILESCANNER AS SYS.IN THEN AS INPUT AVOIDED NON DECLARED IOException
    // NEXT TRY CATCH WILL NOT FAIL. createFileInst(file, true) above validates file name
    try {
      fileScanner = new Scanner(userInputFile);                                   // Creates scanner instance based off file instance
    } catch (Exception e) {
      System.out.println("FileNotFoundException thrown.");
    }

    int currentLibSize = LIB_SIZE;													// A variable count of library size for expansion if necessary
    while (fileScanner.hasNext()) {
      String[] lineSplit = readInLineSplit(fileScanner);                            // Get Line split
      Boolean hasRating = true;                                                     // hasRating uses length of array to decide if movie has rating
      if (lineSplit.length != 9) {
        hasRating = false;                                                          // If false, constructor leaves rating as empty
      }
      Movie_18347500 tempMovieObj = new Movie_18347500(lineSplit, hasRating);       // Doesn't have a rating

      outputArray[arrayCounter] = tempMovieObj;                                     // Assign object to array
      arrayCounter ++;                                                              // update counter for next index
      if (arrayCounter == currentLibSize) {										    
        outputArray = Arrays.copyOf(outputArray, arrayCounter + 10);                // Ensures when reading in, library isn't filled
        currentLibSize += 10;														// New updated library size
      }
    }

    // FILL IN THE REST OF LIBRARY WITH BLANK MOVIES
    for (int blankMovieIndex = arrayCounter; blankMovieIndex < currentLibSize; blankMovieIndex++) {
      outputArray[blankMovieIndex] = new Movie_18347500();
    }

    movieFileName = userInputFile.getName();
    fileScanner.close();
    return outputArray;

  }

  /**
   * Returns the array populated with playlist information for playlists to adopt
   *
   * @param fileName  name of the file on the computer to read from e.g. myFile.txt
   * @return          Array of playlist objects to become the playlists library
   */
  static Playlist_18347500[] initialisePlaylistList(String fileName) {
    Playlist_18347500[] outputArray = new Playlist_18347500[LIB_SIZE];        // OUTPUT list created
    int arrayCounter = 0;


    File userInputFile = createFileInst(fileName, false);                    // File created. Dp not persist in finding pre-existing file
    Scanner fileScanner = new Scanner(System.in);                            // Creating keyboard input first allows try catch for FNF and also 'may not be initialised issue'
    // CANT THROW IOException OUTSIDE OF METHOD, DEFINING FILESCANNER AS SYS.IN THEN AS INPUT AVOIDED NON DECLARED IOException
    // NEXT TRY CATCH WILL NOT FAIL. createFileInst(file, true) above validates file name
    try {
      fileScanner = new Scanner(userInputFile);                                     // Creates a scanner instance based off file instance
    } catch (Exception e) {
      System.out.println("FileNotFoundException thrown.");
    }

    int currentLibSize = LIB_SIZE;											      // Keeps a variable count of max library size
    while (fileScanner.hasNext()) {
      String[] lineSplit = readInLineSplit(fileScanner);                          // Splits the line up by commas
      int playlistID = Integer.parseInt(lineSplit[0]);                            // Playlist ID
      String playlistName = lineSplit[1];                                         // Playlist Name
      int playlistLength = Integer.parseInt(lineSplit[2]);                        // How many movies
      int[] moviesInPlaylist = getPlaylistMovIDs(lineSplit, playlistLength);      // Takes all movie int in a sublist
      float totalDuration = sumMovieDurations(moviesInPlaylist);                  // Created after moviesInPlaylist for obvious reason

      Playlist_18347500 tempPlaylistObj = new Playlist_18347500(
        playlistID,                                                // int playlistID
        playlistName,                                              // String playlistName
        playlistLength,                                            // int length (how many movies)
        totalDuration,                                             // float sum of duration
        moviesInPlaylist                                           // int[] movies in playlist
      );

      outputArray[arrayCounter] = tempPlaylistObj;					// Assign playlist object to an array index
      arrayCounter ++;												// Update arrayCounter

      if (arrayCounter == currentLibSize) {
        outputArray = Arrays.copyOf(outputArray, arrayCounter + 10);                // Ensures when reading in, library isn't filled
        currentLibSize += 10;												        // Keeps an updated count of max lib size
      }
    }

    // FILL IN THE REST OF LIBRARY WITH BLANK MOVIES
    for (int blankPlaylistIndex = arrayCounter; blankPlaylistIndex < currentLibSize; blankPlaylistIndex++) {
      outputArray[blankPlaylistIndex] = new Playlist_18347500();
    }

    playlistFileName = userInputFile.getName();
    fileScanner.close();
    return outputArray;
  }

  /**
   * Writes data from the movieLibrary array to the fileName given.
   *
   * @param fileName  name of the file on the computer to write to e.g. myFile.txt
   */
  static void saveMovies(String fileName) throws IOException{

    PrintWriter outMovies = new PrintWriter(fileName);				
    int count = 0;													// Counts how many lines are output to file

    for (Movie_18347500 movie : movieLibrary) {
      if (movie.getMovieID() != -1) {                                 // Selects all Valid Movies for saving

        String movieRating;
        if (movie.getMovieRating() == -1) {                           // Undefined ratings are made blank
          movieRating = "";
        }
        else if (movie.getMovieRating() % 1 == 0) {                   // X.0 ratings are shortened to X
          movieRating = String.valueOf((int)movie.getMovieRating());
        }
        else {                                                        // X.5 ratings are left as is
          movieRating = String.valueOf(movie.getMovieRating());
        }

        String movieLine = movie.getMovieID() + "," +
                           movie.getMovieName() + "," +
                           movie.getMovieDirector() + "," +
                           movie.getMovieWriter() + "," +
                           movie.getMovieDuration() + "," +
                           movie.getMovieGenre() + "," +
                           movie.getMovieClassification() + "," +
                           movie.getMovieRelease() + "," +
                           movieRating;
        outMovies.println(movieLine);
        count++;
      }
    }
    outMovies.close();
    System.out.println("Successfully saved " + count + " movies to " + fileName);
  }

  /**
   * Writes data from the playlists array to the fileName given
   *
   * @param fileName  name of the file on the comptuer to write to e.g. myFile.txt
   */
  static void savePlaylists(String fileName) throws IOException{

    PrintWriter outPlaylists = new PrintWriter(fileName);
    int count = 0;													// Counts how many lines are output to file

    for (Playlist_18347500 playlist : playlists) {
      if (playlist.getPlaylistID() != -1) {                         // Excluding blank playlists

        String playlistMovIDs = "";                                 // CONCATENATING MOV IDs
        for (int movID : playlist.getPlaylistMovies()) {
          if (movID != -1) {
            playlistMovIDs += "," + movID;							// Excluding blank movies in playlist
          }
        }
        String playlistLine = playlist.getPlaylistID() + "," +
                              playlist.getPlaylistName() + "," +
                              playlist.getPlaylistLength() +
                              playlistMovIDs;
        outPlaylists.println(playlistLine);
        count++;
      }
    }
    outPlaylists.close();
    if (count == 0) {
      System.out.println("Action successfull but there were no playlists to save.");
    }
    System.out.println("Successfully saved " + count + " playlists to " + fileName);
  }


// ## MUTATING DATA ##
// ###################

  // Sorts the movie library alphabetically
  /**
   * Copies the movieLibrary array and sorts it alphabetically
   *
   * @return         Array of sorted movie objects for printing
   */
  static Movie_18347500[] sortByTitle() {

    // Display movies by title
    Movie_18347500[] alphaSorted = Arrays.copyOf(movieLibrary, numMovieIndexes());               // Creates a List to manipulate
    Arrays.sort(alphaSorted, Movie_18347500.COMPARE_BY_NAME);  									 // Sorts based on name.
    return alphaSorted;                                        									 // returns the Movies in order of sort

  }

  /**
   * Copies the movieLibrary array and sorts it by genre then alphabetically
   * to have it alphabetical within an alphabetical genre sort
   *
   * @return         Array of sorted movie objects for printing
   */
  static Movie_18347500[] sortByGenre() {

    Movie_18347500[] genreSorted = Arrays.copyOf(movieLibrary, numMovieIndexes());
    Arrays.sort(genreSorted, Movie_18347500.COMPARE_BY_NAME);          							// Sort sort by name first
    Arrays.sort(genreSorted, Movie_18347500.COMPARE_BY_GENRE);         							// Sorts by genre second so that in a genre movies are alphabetical
    return genreSorted;
  }

  /**
   * Assigns movieLibrary array to a copy of itself with more indexes.
   * It also fills new empty spaces with blank movie objects
   */
  static void expandMovieArray() {
    int originalSize = numMovieObjects();                             							// To reference where to start filling in blank movies later
    movieLibrary = Arrays.copyOf(movieLibrary, originalSize + EXPAND_SIZE);    					// Copy movieLibrary to itself but larger
    for (int i = originalSize; i < originalSize + EXPAND_SIZE; i++) {         					// Fill in blanks
      movieLibrary[i] = new Movie_18347500();
    }
  }

  /**
   * Assigns playlists array to a copy of itself with more indexes
   * It also fills new empty spaces with blank playlsit obejcts
   */
  static void expandPlaylistArray() {
    int originalSize = numPlaylistObjects();                                   					// Where to start filling in blank playlists
    playlists = Arrays.copyOf(playlists, originalSize + EXPAND_SIZE);          					// Copy playlists to itself but larger
    for (int i = originalSize; i < originalSize + EXPAND_SIZE; i++) {          					// Fill in blanks
      playlists[i] = new Playlist_18347500();
    }
  }

  /**
   * Adds a movie to the next available index in a playlists movie array.
   * Expands the playlists movie array if needed
   */
  static void addMovieToPlaylist() {
    // find playlist index
    int playlistIndex;
    do {
      playlistIndex = findPlaylistIndex();
    } while (playlistIndex == -1);
    // find movie index
    int movieIndex;
    do {
      movieIndex = findMovieIndex() + 1;                          // Off by one fix
    } while (movieIndex == -1);

    // Expanding playlist movie list length if full
    if (playlists[playlistIndex].getPlaylistLength() == playlists[playlistIndex].getPlaylistTotalLength()) {
      playlists[playlistIndex].expandPlaylist();
    }

    // Add movie to list
    playlists[playlistIndex].addToPlaylist(movieIndex);

    // Set Duration to sum of new movie list
    playlists[playlistIndex].setDuration(sumMovieDurations(playlists[playlistIndex].getPlaylistMovies()));

    System.out.println("\n\t!! Movie {" + movieLibrary[movieIndex-1].getMovieName() + "} was added to playlist {" + playlists[playlistIndex].getPlaylistName() + "}");        // Strange off by one error
  }

  /**
   * Adds a movie to the next available index in movieLibrary array
   * Expands the movieLibrary array if needed
   */
  static void addMovieSubMenu() throws IOException  {
    // Create temp variables
    // Create Movie_18347500 object
    // Add object to movieLibrary
    int movieIDNum = findMaxMovieID() + 1;                    // Ensures created ID is not a duplicate
    String title = getStrIn("    Movie Title: ");             // Movie Title    NO VALIDATION
    String director = getStrIn("       Director: ");          // Director       NO VALIDATION
    String writer = getStrIn("         Writer: ");            // Writer         NO VALIDATION
    float duration = getFloatIn("   Duration (H): ", false);  // Duration       FLOAT VALIDATION
    
    String genre;                                             // Genre          GENRE VALIDATION. USED IN genreSubMenu
    do {
      printGenres();
      genre = getStrIn("          Genre: ");
    } while (!isValidGenre(genre));
    
    String classification = getStrIn(" Classification: ");    // Classification NO VALIDATION
    System.out.println("Date Format (DD MONTH YEAR)");
    String releaseDate = getStrIn("   Release Date: ");       // Release Date   NO VALIDATION
    float rating;                                             // Rating         RATING VALIDATION. USED IN ratingSubMenu
    do {
      rating = getFloatIn("         Rating: ", true);
    } while (!isValidRating(rating));

    Movie_18347500 tempMovieObj = new Movie_18347500(
      movieIDNum,
      title,
      director,
      writer,
      duration,
      genre,
      classification,
      releaseDate,
      rating
    );

    if (numMovieObjects() == numMovieIndexes()) {
      expandMovieArray();
    }

    movieLibrary[movieIDNum - 1] = tempMovieObj;              // Assigns film to next available index because ID is off by one of indexes

  }


//## MENU MENU MENU MENU MENU ##
//##############################

// ## MENU FUNCTIONALITY

  /**
   * This method starts the menu selection and in turn allows
   * all other functionality.
   *
   * @param   movieLibrary an array of Movie_18347500 objects which is the entire library
   * @param   playlists    an array of Playlist_18347500 objects which are the playlists of movies
   */
  static void rootMenu(Movie_18347500[] movieLibrary, Playlist_18347500[] playlists) throws IOException{

    // Menu Loop Flags
    Boolean inRootMenu = true;              // Root Menu Loop Flag
    Boolean inMoviesMenu = false;           // Movie Menu Loop Flag
    Boolean inSortSubMenu = false;          // Sort Sub Menu Loop Flag
    Boolean inPlaylistsMenu = false;        // Playlists Menu Loop Flag

    // Root Menu Loop, when false, end program
    while (inRootMenu) {

      // Print Menu options and then take input
      printRootMenuOptions();
      int inputChoice = getIntIn("Enter Menu Choice: ");

      // Deciding on Menu options
      switch (inputChoice) {

        // MOVIES MENU
        case 1:
          // Loop through movies menu till false flag
          inMoviesMenu = true;
          do {
            inMoviesMenu = moviesMenu(inMoviesMenu, inSortSubMenu);
          } while (inMoviesMenu);
          break;

        // PLAYLISTS MENU
        case 2:
          // Loop through playlists menu till false flag
          inPlaylistsMenu = true;
          do {
            inPlaylistsMenu = playlistsMenu(inPlaylistsMenu);
          } while (inPlaylistsMenu);
          break;

        // SAVE MENU
        case 3:
          saveMenu();
          break;

        // EXIT MENU
        case 4:
          // Sets loop flag to false
          inRootMenu = false;
          break;

        // Bad input catch
        default:
          System.out.println("ERROR: Invalid Input. Please enter a menu choice");
          break;

      }

    }

  }

  /**
   * Moves the user to the movie specific menu
   *
   * @param inMoviesMenu   Boolean to tell rootMenu whether to call movieMenu
   * @param inSortSubMenu  Boolean to tell movieMenu whether to call sortSubMenu
   * @return               Boolean tells rootMenu whether to call movieMenu again
   */
  static Boolean moviesMenu(Boolean inMoviesMenu, Boolean inSortSubMenu) throws IOException{

    // Print menu options and take input
    printMoviesMenuOptions();
    int inputChoice = getIntIn("Enter Menu Choice: ");

    switch (inputChoice) {

      // DISPLAY
      case 1:
        // Display Movies in ID order
        displayAllMovies(movieLibrary);
        break;

      // SORT BY MENU
      case 2:
        // Loop through Sort by menu till false flag
        inSortSubMenu = true;
        do {
          inSortSubMenu = sortSubMenu(inSortSubMenu);
        } while (inSortSubMenu);
        break;

      // RATING MENU
      case 3:
        ratingSubMenu();
        break;

      // GENRE MENU
      case 4:
        genreSubMenu();
        break;

      // ADD MOVIE
      case 5:
        addMovieSubMenu();
        break;

      // EXIT
      case 6:
        // Sets loop flag to false
        inMoviesMenu = false;
        break;

      default:
        // Bad input catch
        System.out.println("ERROR: Invalid Input. Please enter a menu choice");
        break;

    }
    return inMoviesMenu;

  }

  /**
   * Moves the user to the playlist specific menu
   *
   * @param inPlaylistsMenu  Boolean to tell rootMenu whether to call playlistsMenu
   * @return                 Boolean tells rootMenu whether to call playlistsMenu again
   */
  static Boolean playlistsMenu(Boolean inPlaylistsMenu) throws IOException{

    // Print out menu options and takes input
    printPlaylistsMenuOptions();
    int inputChoice = getIntIn("Enter Menu Choice: ");

    switch (inputChoice) {

      // DISPLAY
      case 1:
        // Display Playlists
        displayAllPlaylists(playlists);
        break;

      // CREATE
      case 2:
        // Create a playlist
        addPlaylistSubMenu();
        break;

      // ADD TO PLAYLIST
      case 3:
        // Add a movie to a playlist
        addMovieToPlaylist();
        break;

      // EXIT
      case 4:
        // Sets loop flag to false
        inPlaylistsMenu = false;
        break;

      default:
        // Bad input catch
        System.out.println("ERROR: Invalid Input. Please enter a menu choice");
        break;
    }

    return inPlaylistsMenu;
  }

  /**
   * Moves the user to the sort specific menu
   *
   * @param inSortSubMenu  Boolean to tell moviesMenu whether to call inSortSubMenu
   * @return               Boolean tells moviesMenu whether to call inSortSubMenu again
   */
  static Boolean sortSubMenu(Boolean inSortSubMenu) throws IOException{

    // print menu options and take input
    printSortMenuOptions();
    int inputChoice = getIntIn("Enter Sort by choice: ");

    switch (inputChoice) {

      // TITLE
      case 1:
        // Display movies by title
        displayAllMovies(sortByTitle());                            // Displays the Movies based in order of title sort
        break;

      // GENRE
      case 2:
        // Display movies by genre
        displayAllMovies(sortByGenre());
        break;

      // EXIT
      case 3:
        // sets loops flag to false
        inSortSubMenu = false;
        break;

      default:
        // catch bad input
        System.out.println("ERROR: Invalid Input. Please enter a menu choice");
        break;
    }

    return inSortSubMenu;
  }

  /**
   * Moves the user to the rating specific menu. Changes the rating of a specific movie
   */
  static void ratingSubMenu() throws IOException{
    int movieIndex;								
    do {
      movieIndex = findMovieIndex();																					// Finds a valid movie index
    } while (movieIndex == -1);

    System.out.println("\n\t!! This movies rating is " + movieLibrary[movieIndex].getMovieRating() + "\n");

    float rating;
    do {
      rating = getFloatIn("Please enter the new rating: ", true);														// Takes new rating and validates it
    } while (!isValidRating(rating));

    movieLibrary[movieIndex].setMovieRating(rating);
    String tempRating = displayRating(movieLibrary[movieIndex].getMovieRating());
    System.out.println("Movie {" + movieLibrary[movieIndex].getMovieName() + "} rating changed to " + tempRating);		// confirmation of changed rating

  }

  /**
   * Moves the user to the genre specific menu. Changes the genre of a specific movie
   */
  static void genreSubMenu() {
    int movieIndex;
    do {
      movieIndex = findMovieIndex();																					// Finds a valid movie index
    } while (movieIndex == -1);

    System.out.println("\n\t!! This movies genre is " + movieLibrary[movieIndex].getMovieGenre());

    String newGenre;
    do {
      printGenres();
      newGenre = getStrIn("Please enter a genre from above: ");															// Takes a new genre and validates it
    } while (!isValidGenre(newGenre));

    movieLibrary[movieIndex].setMovieGenre(newGenre);																	// Confirmation of changed genre
    System.out.println("\n\t!! Movie {" + movieLibrary[movieIndex].getMovieName() + "} genre changed to " + movieLibrary[movieIndex].getMovieGenre());
  }

  /**
   * Moves the user to the adding playlist menu.
   * Creates a playlist and adds it the playlists array
   */
  static void addPlaylistSubMenu() throws IOException {
    int playlistIDNum = findMaxPlaylistID() + 1;                // Ensures no ID is a duplicate
    String name = getStrIn(" Playlist Name: ");                 // Name NO VALIDATION

    Playlist_18347500 tempPlaylistObj = new Playlist_18347500(
      playlistIDNum,
      name
    );

    if (numPlaylistObjects() == numPlaylistIndexes()) {
      expandPlaylistArray();									// Expand if necessary
    }

    playlists[playlistIDNum - 1] = tempPlaylistObj;             // Array index is ID num off by one
  }

  /**
   * Moves user to save specific menu
   */
  static void saveMenu() throws IOException{
    System.out.println("\n\tSAVING MOVIES");
    saveMovies(movieFileName);
    System.out.println("\n\tSAVING PLAYLISTS");
    savePlaylists(playlistFileName);
    // OVERWRITES FLIES WITH DATA IN MEMORY

  }



// ## DISPLAY | DISPLAY | DISPLAY ##
// #################################

  /**
   * Displays all needed attributes from an input array
   *
   * @param inputList  An array of Movie objects that may be sorted or not
   */
  static void displayAllMovies(Movie_18347500[] inputList) {
    for (Movie_18347500 movie : inputList) {

      String checkedRating = displayRating(movie.getMovieRating());             // Better formatting of the rating and blanks option
      if (movie.getMovieID() >= 0) {
        System.out.println(movie.getMovieID() + ". " + movie.getMovieName() + " " + checkedRating + " [Released: " + movie.getMovieRelease() + "]");
        System.out.println("\tDURATION: " + movie.getMovieDuration() + " hours\t\tWRITER: " + movie.getMovieWriter());
        System.out.println("\tCLASSIFICATION: " + movie.getMovieClassification() + "\t\tDIRECTOR: " + movie.getMovieDirector());
        System.out.println("\tGENRE: " + movie.getMovieGenre() + "\n");
      }
    }
  }

  /**
   * Displays all the information in the playlist Array
   *
   * @param inputList  An array of playlist objects
   */
  static void displayAllPlaylists(Playlist_18347500[] inputList) {
  for (Playlist_18347500 playlist : inputList) {
    if (playlist.getPlaylistID() > 0) {
      System.out.println("\n" + playlist.getPlaylistID() + ". " + playlist.getPlaylistName() + " ["+ playlist.getPlaylistLength() + " movies] {Total Runtime: " + playlist.getPlaylistDuration() + " hours}");
      if (playlist.getPlaylistLength() > 0) {
        for (int playlistMovieID : playlist.getPlaylistMovies()) {          							   // Loops though playlistMovie IDs
          if (playlistMovieID > 0) {
            for (Movie_18347500 libMovie : movieLibrary) {                                                 // Loops through movieLibrary movies
              if (playlistMovieID == libMovie.getMovieID()) {                                              // checks playlistMovieID against movieLibraryID
                System.out.println("\t" + libMovie.getMovieID() + ". " + libMovie.getMovieName());         // prints out movie name
              }
            }
          }
        }
      }
    }
  }
}

// ## PRINTED MENU OPTIONS

  /**
   * Prints Root menu options
   */
  static void printRootMenuOptions() {
    System.out.println("");
    System.out.println("-- MAIN MENU --");
    System.out.println("---------------");
    System.out.println("- 1. Movies");
    System.out.println("- 2. Playlists");
    System.out.println("- 3. Save");
    System.out.println("- 4. Exit");
  }

  /**
   * Prints movies menu options
   */
  static void printMoviesMenuOptions() {
    System.out.println("");
    System.out.println("-- MOVIES MENU --");
    System.out.println("-----------------");
    System.out.println("- 1. Display Movies");
    System.out.println("- 2. Sort Movies");
    System.out.println("- 3. Change Movie Rating");
    System.out.println("- 4. Change Movie Genre");
    System.out.println("- 5. Add Movie");
    System.out.println("- 6. Go Back");
  }

  /**
   * Prints playlist menu options
   */
  static void printPlaylistsMenuOptions() {
    System.out.println("");
    System.out.println("-- PLAYLISTS MENU --");
    System.out.println("--------------------");
    System.out.println("- 1. Display Playlists");
    System.out.println("- 2. Create a Playlist");
    System.out.println("- 3. Add Movie to a Playlist");
    System.out.println("- 4. Go Back");
  }

  /**
   * Prints sort menu options
   */
  static void printSortMenuOptions() {
    System.out.println("");
    System.out.println("-- SORT MENU --");
    System.out.println("---------------");
    System.out.println("- 1. Sort Movies by Title");
    System.out.println("- 2. Sort Movies by Genre");
    System.out.println("- 3. Go Back");
  }

  /**
   * Prints genre menu options
   */
  static void printGenres() {
    System.out.println("");
    System.out.println("Available Genres:");
    System.out.println("\t- Action\t- Adventure\t- Comedy");
    System.out.println("\t- Crime\t\t- Fantasy\t- Family");
    System.out.println("\t- Romance\t- Horror\t- Drama");
    System.out.println("\t- Sci-fi\t- Thriller");
  }

  /**
   * Takes the movies rating and returns the appropriate format
   *
   * @param rating  The rating help by the movie object
   * @return        The correctly formatted rating for display
   */
  static String displayRating(float rating) {
    String tempRating;
    if (rating == (float)(-1)) {
      tempRating = "{NO RATING}";				// Blank Rating Change
    }
    else {
      tempRating = "{" + rating + "/5}";		// Non blank rating change
    }
    return tempRating;
  }


// ## RETRIEVE INFO RETREIVE INFO
// ##############################

// ## DATA IN FROM USER

  /**
   * Retrieves the index of the searched movie
   *
   * @param searchKey  String of the movie used for searching
   * @return           Movie index or -1 for not found
   */
  static int searchForMovieIndex(String searchKey) {

    // Objects.equals(A,B); will give value equality boolean
	// String strA == strB doesn't refer to content
    int outputIndex = -1;
    for (int i = 0; i < numMovieObjects(); i++) {
      if (Objects.equals(searchKey, movieLibrary[i].getMovieName())) {
        return i;
      }
    }

    System.out.println("No movies named " + searchKey + " were found. Please change your search term.");
    return outputIndex;
  }

  /**
   * Retrieves the index of the searched playlist
   *
   * @param searchKey  String of the playlist used for searching
   * @return           Playlist index or -1 for not found
   */
  static int searchForPlaylistIndex(String searchKey) {
    int outputIndex = -1;
    for (int i = 0; i < numPlaylistObjects(); i++) {
      if (Objects.equals(searchKey, playlists[i].getPlaylistName())) {			// Returns movie index if found
        return i;
      }
    }

    System.out.println("No playlists with the name " + searchKey + " were found. Please change your search term.");
    return outputIndex;															// Returns -1 otherwise
  }	

  /**
   * Gets a string from the user based on a prompt. No validation on input
   *
   * @param prompt  String of message to present user before input
   * @return        String of user input
   */
  static String getStrIn(String prompt) {
    System.out.print(prompt);
    String output = kb.nextLine();					// Takes string after prompt
    return output;
  }

  /**
   * Gets an integer from the user based on a prompt. Validation used to
   * ensure a correct input
   *
   * @param prompt  String of message to present user before input
   * @return        Integer of user input
   */
  static Integer getIntIn(String prompt) throws IOException{

    // Takes string input to validate as Int
    String outStr = "";
    // Keeps asking for input while not a valid Integer
    while (!NumberValidation_18347500.isInt(outStr)) {
      outStr = getStrIn(prompt);
      // Only if the loop is going to repeat again does it show an error
      if (!NumberValidation_18347500.isInt(outStr)) {
        System.out.println("ERROR: Not a valid integer. Please enter another choice.");
      }
    }
    return Integer.parseInt(outStr);			// Returned as Integer not cast as int because of primitive data type issues
  }

  /**
   * Gets an float from the user based on a prompt. Validation used to
   * ensure a correct input
   *
   * @param prompt  String of message to present user before input
   * @return        Float of user input
   */
  static Float getFloatIn(String prompt, Boolean isRating) throws IOException {

    // Takes string input to validate as float
    String outStr = "";
    // keeps asking for input while not a valid float
    while (!NumberValidation_18347500.isFloat(outStr)) {
      outStr = getStrIn(prompt);
      if (Objects.equals(outStr, "") && isRating) {     // Rating can be a blank value
        return (float)(-1);
      }
      // only if the loop is going to repeat again does it show an error
      if (!NumberValidation_18347500.isFloat(outStr)) {
        System.out.println("ERROR: Not a valid decimal number. Please enter another choice.");
      }
    }
    return Float.parseFloat(outStr);
  }

  /**
   * Splits up the information from a read in line
   *
   * @param inScanner  Scanner reading from a file
   * @return           Array of next line with String tokens
   */
  static String[] readInLineSplit(Scanner inScanner) {
    // All input is seperated by a ',' in files
    String iterLineText = inScanner.nextLine();
    String[] parts = iterLineText.split(",");
    return parts;
  }

// ## DATA ABOUT ARRAYS
  /**
   * Counts the number of movies in the library
   *
   * @return        Integer of movie count
   */
  static int numMovieObjects() {
    int movieCounter = 0;
    for (Movie_18347500 movie : movieLibrary) {
      if (movie.getMovieID() != -1) {
        movieCounter ++;
      }
    }
    return movieCounter;
  }

  /**
   * Counts the number of movie library array indexes. Includes blank movies
   *
   * @return        Integer of movie library indexes (including blank movies)
   */
  static int numMovieIndexes() {
    int movieCounter = 0;
    for (Movie_18347500 movie : movieLibrary) {
      movieCounter ++;
    }
    return movieCounter;
  }

  /**
   * Counts the number of playlists in the playlists array.
   *
   * @return        Integer of playlists (excluding blank playlists)
   */
  static int numPlaylistObjects() {
    int playlistCounter = 0;
    for (Playlist_18347500 playlist : playlists) {
      if (playlist.getPlaylistID() != -1) {
        playlistCounter ++;
      }
    }
    return playlistCounter;
  }

  /**
   * Counts the number of playlists array indexes. Includes blank playlists
   *
   * @return        Integer of playlists array index (including blank playlists)
   */
  static int numPlaylistIndexes() {
    int playlistCounter = 0;
    for (Playlist_18347500 playlist : playlists) {
      playlistCounter ++;
    }
    return playlistCounter;
  }

  /**
   * Retrieves the index of a searched movie.
   *
   * @return        Integer of movie index
   */
  static int findMovieIndex() {
    // SEARCH THEN SET RATING
    int movieIndex;
    do {
      movieIndex = searchForMovieIndex(getStrIn("Please enter a movie title: "));   // Returns movie index if exists
    } while (movieIndex == -1);

    return movieIndex;
  }

  /**
   * Retrieves the index of a searched playlist
   *
   * @return        Integer of playlist index
   */
  static int findPlaylistIndex() {
    // SEARCH THEN RETURN index
    int playlistIndex;
    do {
      playlistIndex = searchForPlaylistIndex(getStrIn("Please enter a playlist title: "));
    } while (playlistIndex == -1);

    return playlistIndex;
  }

  /**
   * Retrieves the largest movies ID
   *
   * @return        Integer of movie ID
   */
  static int findMaxMovieID() {
    int biggest = -1;
    for (Movie_18347500 movie : movieLibrary) {
      if (movie.getMovieID() > biggest) {
        biggest = movie.getMovieID();
      }
    }
    return biggest;
  }

  /**
   * Retrieves the largest playlist ID
   *
   * @return        Integer of playlist ID
   */
  static int findMaxPlaylistID() {
    int biggest = -1;
    for (Playlist_18347500 playlist : playlists) {
      if (playlist.getPlaylistID() > biggest) {
        biggest = playlist.getPlaylistID();
      }
    }
    return biggest;
  }

  /**
   * Adds all the playlist's movies to find a total duration
   *
   * @return        Float of total movies durations
   */
  static float sumMovieDurations(int[] moviesInPlaylist) {
    float totalDuration = 0;                          // Running count of movie durations
    for (int playlistMov : moviesInPlaylist) {
      for (Movie_18347500 mov : movieLibrary) {
        if (mov.getMovieID() >= 0) {                  // Movies with -1 ID are unassigned
          if (playlistMov == mov.getMovieID()) {      // Comparing playlistMovieID with IDs form Library
            totalDuration += mov.getMovieDuration();
          }
        }
      }
    }
    return totalDuration;
  }

  /**
   * Extracts the movie IDs from a playlist from the input line
   *
   * @return        Integer of playlists (excluding blank playlists)
   */
  static int[] getPlaylistMovIDs (String[] inStringArray, int playlistLength) {
    int[] outIntArray = new int[LIB_SIZE];
    for (int a = 0; a < LIB_SIZE; a++) {             // Assigns all movies ID of -1 so if unused, not seen 'as a movie'
      outIntArray[a] = -1;
    }
    int outInArrayCounter = 0;
    int maxMovieIndex = 3 + playlistLength;
    for (int i = 3; i < maxMovieIndex; i++) {
      outIntArray[outInArrayCounter] = Integer.parseInt(inStringArray[i]);      // Parse and not cast >> error: incompatible types: String cannot be converted to int
      outInArrayCounter ++;
    }
    return outIntArray;
  }


// ## CHECKS AND VALIDATION               
// ########################

  /**
   * Checks if the rating is valid
   * - 0 <= rating <=5
   * - Divisable by 0.5 with no remainder
   * - or -1 for a non rating
   *
   * @return        Boolean of valid rating or not
   */
  static Boolean isValidRating(float rating) {
    if (rating == -1.0) {
      return true;                                                          // Blank rating flag
    }
    if ((0.0 <= rating && rating <= 5.0) && rating % 0.5 == 0) {            // Whole and half numbers can be divided by 0.5 with no remainder
      return true;                                                          // Only whole and half number can be ratings
    }
    else {
      System.out.println("\n # " + rating + " is not a valid rating.");
      System.out.println("\tRATING MUST BE\n\t - Between 0 and 5\n\t - A whole or half number\n\t - e.g. 0,1.5 or 4.0\n\t - Blank for a non rating");
      return false;
    }
  }

  /**
   * Checks the genre against a list of set genres
   *
   * @return        Boolean of valid genre or not
   */
  static Boolean isValidGenre(String newGenre) {
    String[] genres = {"Action", "Adventure", "Comedy", "Crime", "Fantasy", "Family", "Romance", "Horror", "Drama", "Sci-fi", "Thriller"};
    for (String validGenre : genres) {
      if (Objects.equals(newGenre.toLowerCase(), validGenre.toLowerCase())) {
        return true;
      }
    }
    System.out.println(newGenre + " is not a valid genre. Please make another selection.");
    return false;
  }
}
