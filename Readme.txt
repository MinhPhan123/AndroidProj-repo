Group name: Team 16

I. Work Distribution
	1. Phan Nhat Minh -s3904422
		Set up and implement database shop item
		Frontend design for landing page, navigation drawer in landing page, category, item list, search activity, product view layout.
		Backend coding for landing page(aka. Main Activity), ItemList activity (include recycler view holder), SearchActiity (include search algorithm), ProductViewAdapter for RecyclerView in landing page, design Item object. 
		Linking activity from navigation drawer into coresponding activity. 
		Display auto carousel image slider for landing page and item description. 
		Debug item receive into the ViewItemDetailActivity.
		Debug update view after add and remove item from cart and wishlist

	2. Nguyen Minh Hien -3877996
		Frontend design for Splash screen, Login/Register, Chat box, Profile, Messages Adapter (display user), Chat Adapter (display conversation messages)
		Backend coding for Register/Login (with normal resgister with mail verify and Google mail, foget password); 
		Display profile, upload avatar(Profile) & Edit the user's information (EditProfile); 
		Chat funtion with the recycler view to display the users and the chat (Cgat List, Chat Adapter, Chat, ChatActivity, MessageList, MessageAdapter) 
		
	3. Trinh Van Minh Duc -3915177
		Frontend design for app icon, cart , wishlist, Ongoing and order history
		Backend coding for broadcast receiver(warning no wifi signal), auto logout service
		Design and implement cart, wishlist, on going order, order history activity
		
	4. Phan Duy Anh -s3802674
		Front end design for view item detail page, payment page, no internet connection alert dialog
		Back end coding for ViewItemDetailActivity, linking between ItemList activity, SearchActivity with ViewItemDetail activity, Item object.

II. Functionalities
	1. SignIn/SignUp and LogOut with normal email verify
	   and with Google email & Forget password (password recorvery)
	2. Search for item
	3. Display item list base on category
	4. View item description
	5. Add item to cart/ whislist
	6. ChatBox
	7. View and update profile
	8. View ordered history
	9. View ongoing order
	10.Auto log out after 1 minute
	11.Warning when there's no wifi signal
	
III. Technology Use
	1. Firebase component (Firestore Database, Realtime Database, Storage, Authentication)
	2. Carosel Motion Layout from Denzcoskum
	3. Image Display from Picasso Lib
	
IV. Open issues and known bugs
	1. Need to press back 2 times to return from chat message to the main menu (emulator bug)
	2. Service auto log out in the item describtion
	3. Quantity not apply into the total price
