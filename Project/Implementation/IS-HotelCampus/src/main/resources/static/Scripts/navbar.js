let burgerMenuIsClick = true;


function showMenu(){
	
	const burgerMenu = document.getElementById("burger__menu");
	const menuContent = document.getElementById("menu__content");
	const oscuraSfondo = document.getElementById("oscuraSfondo");
	
	if(burgerMenuIsClick){
		burgerMenuIsClick = false;
		burgerMenu.className = "burger_menu__close__active";
		oscuraSfondo.className = "oscuraSfondoAttivo";
		menuContent.className = "showMenu";		
			
	
	}else{
		burgerMenuIsClick = true;
		oscuraSfondo.className = "oscuraSfondo";

		menuContent.className = "menustandard";	
		burgerMenu.className = "";	
	}
	
}




