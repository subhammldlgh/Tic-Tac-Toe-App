# Tic Tac Toe

(a) 
Name of the project: Tic Tac Toe App

Name: Subham Kumar

BITS ID: 2018B5A70887G

Email: f20180887@goa.bits-pilani.ac.in


(b)
This is an application which resembles the basic Tic Tac Toe game. It uses Android Navigation Component,
with a single activity and three fragments. We have to login in the application using the email id
and password. If not already registered then we register the user. Now the user is
shown the dashboard which has the welcome message with wins and losses for that particular user.
It also shows a list of players who are waiting for the second player to join for a two player online 
tic tac toe game and a floating button that lets the current user create a new one player or two player
game by showing an alert dialog. Now the user enters a game by any of the above methods which may be 
single player, two player first user or second player second user. The current user can now play the 
game and if we press back button before the final decision of game, an alert dialog pops up to give
options of cancel or accept the defeat. Now the data is updated in the dashboard that shows the 
number of wins and losses for that particular user.
Some known bugs are concurrency when two users try to open the same already created game.
Some screenshots are:
 
<img width="394" alt="Screenshot 2021-12-07 at 11 27 01 PM" src="https://user-images.githubusercontent.com/66794059/145082186-ca50c86f-1fdb-46f8-8c19-c4b3c63908da.png">
<img width="397" alt="Screenshot 2021-12-07 at 11 27 16 PM" src="https://user-images.githubusercontent.com/66794059/145082191-22b41b50-5b03-4527-b60f-012f76275fd7.png">
<img width="393" alt="Screenshot 2021-12-07 at 11 27 38 PM" src="https://user-images.githubusercontent.com/66794059/145082193-53843d4d-646f-44e2-b3bb-55a4ff3fcf00.png">
<img width="393" alt="Screenshot 2021-12-07 at 11 28 26 PM" src="https://user-images.githubusercontent.com/66794059/145082196-f0f0c42e-7576-40ae-bff1-a2816fed47d9.png">



(c)
Task 1:
The first task is basically managing register or log in and log out from accounts. First, we are in 
dashboard fragment and if no accounts are logged in then we navigate or push the user to login fragment
where preregistered user can log in and enter the dashboard or can register and enter into dashboard.
This is implemented using firebase authentication, where firebase maintains the key value pair of
email and password also giving it a unique UID. Now when we have reached the dashboard fragment it 
should have a logout options in the drop down menu bar.

Task 2:
This is the Single player mode. Here we open the games fragment using the navigation controller. Now 
we have the two players the first one being the user itself and the second player is the computer.
We have on click listeners for all the buttons and we have set it in such a way that computer moves 
only if one of the buttons are pressed. We always wait for the user to move first. As soon as the 
user moves, the computer also plays its turn. Computer uses random function to set its turn. We have 
made a checkgame() function which checks if user or computer wins. As soon as the user wins or losses,
we display a text that says WIN or LOSE. Clicking back button takes us back to the dashboard fragment 
which reflects the corresponding cumulative wins and losses. If the game is draw, it is also displayed
in the text view. User can press the back button as usual to get back to the dashboard.

Task 3:
This is the Two Player mode. Here we open the game fragment for the user who opens a new game using
the floating button as the first player. Other way is to select an already opened game by some other 
user that is displayed in the dashboard. For implementing this we have made a new child for the firebase
database that has Open online games. We extract data from this database for every other user and display
it in the dashboard using recycler view. When we are in the Two Player game, the creator is notified 
by a toast that the second user is ready to play. The game begins as desired and checkgame() is again 
used for checking wins and losses. After the game is over, the respective wins, losses and draws are 
displayed for both the users using if-else ladders. Now if we press the back button for both the users
we remove that game from database so that it is not seen further. The same alert dialog is used 
which was used in one player mode that pops to ask for forfeit or returning back to game.


(d)
The Application first displays the login page which takes the user to dashboard fragment upon registering
or logging in. The floating button creates a new game for the user as desired. The other option is to play a two
player game which is displayed in the dashboard created by other users who are waiting. The game rules 
can is very easy which can be found in https://en.wikipedia.org/wiki/Tic-tac-toe.


(e)
I have not tested the app using instrumented and UI tests but I have tested the app using monkey. Using 
monkey, the application did not crash. I have also used talkback for the application and it worked 
properly. I have also used accessibility scanner on the application and some issues of text size popped 
up which I have corrected it.


(f)
I have taken 70+ hours to complete the assignment.


(g)
I rate this assignment 10 out of 10.