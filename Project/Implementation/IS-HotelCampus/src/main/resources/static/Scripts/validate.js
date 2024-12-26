const nomeCognomeNazionalitaPattern = /^[\p{L}]+(?: [\p{L}]+)*$/u;
const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

const nomeErrorMessage = "Il nome può contenere solo lettere<br/>";
const cognomeErrorMessage = "Il cognome può contenere solo lettere<br/>";
const emailErrorMessage = "Inserire un'email nella forma: username@domain.ext<br/>";
const passwordErrorMessage = "La password deve contere: almeno 8 caratteri, una lettera, una cifra<br/>";
const nazionalitaErrorMessage = "La nazionalità può contenere solo lettere<br/>";

const suggerimentiNazioni = ["albania", "andorra", "armenia", "austria", "azerbaigian", "bielorussia", "belgio",
    "bosnia ed erzegovina", "bulgaria", "croazia", "cipro", "repubblica ceca", "danimarca",
    "estonia", "finlandia", "francia", "georgia", "germania", "grecia", "ungheria", "islanda",
    "irlanda", "italia", "kazakistan", "kosovo", "lettonia", "liechtenstein", "lituania",
    "lussemburgo", "malta", "moldavia", "monaco", "montenegro", "paesi bassi", "macedonia del nord",
    "norvegia", "polonia", "portogallo", "romania", "russia", "san marino", "serbia", "slovacchia",
    "slovenia", "spagna", "svezia", "svizzera", "turchia", "ucraina", "regno unito", "città del vaticano", "altro"];

function validate() {
	
	let valid = true;	
	let form = document.getElementById("regForm");

	let spanNome = document.getElementById("errorNome");
	if(!validateFormElem(form.nome, nomeCognomeNazionalitaPattern, spanNome, nomeErrorMessage)){
		valid = false;
	} 
	let spanCognome = document.getElementById("errorCognome");
	if (!validateFormElem(form.cognome, nomeCognomeNazionalitaPattern, spanCognome, cognomeErrorMessage)){
		valid = false;
	}
	let spanEmail = document.getElementById("errorEmail");
	if (!validateFormElem(form.email, emailPattern, spanEmail, emailErrorMessage)){
		valid = false;
	}
	let spanPassword = document.getElementById("errorPassword");
	if (!validateFormElem(form.password, passwordPattern, spanPassword, passwordErrorMessage)){
		valid = false;
	}
	
	let spanNazionalita = document.getElementById("errorNazionalita");
	let nazionalitaInput = form.nazionalita.value.trim().toLowerCase();
    if (!suggerimentiNazioni.includes(nazionalitaInput)) {
        valid = false;
        spanNazionalita.innerHTML = "Seleziona una nazionalità valida dalla lista di suggerimenti<br/>";
        spanNazionalita.style.color = "red";
        form.nazionalita.classList.add("error");
    } else if (!validateFormElem(form.nazionalita, nomeCognomeNazionalitaPattern, spanNazionalita, nazionalitaErrorMessage)){
		valid = false;
	}
	
	return valid;
}

function validateLogin() {
	
	let valid = true;	
	let form = document.getElementById("formLogin");

	let spanEmail = document.getElementById("errorEmail");
	if (!validateFormElem(form.email, emailPattern, spanEmail, emailErrorMessage)){
		valid = false;
	}
	let spanPassword = document.getElementById("errorPassword");
	if (!validateFormElem(form.password, passwordPattern, spanPassword, passwordErrorMessage)){
		valid = false;
	}
	
	return valid;
}

function validateFormElem(formElem, pattern, span, message) {
	if(!formElem.value.match(pattern)){
		formElem.classList.add("error");
		span.innerHTML = message;
		span.style.color = "red";
		return false;
	}
	formElem.classList.remove("error");
	span.style.color = "black";
	span.innerHTML = "";
	return true;
}

function createXMLHttpRequest() {
	var request;
	try {
		// Firefox 1+, Chrome 1+, Opera 8+, Safari 1.2+, Edge 12+, Internet Explorer 7+
		request = new XMLHttpRequest();
	} catch (e) {
		// past versions of Internet Explorer 
		try {
			request = new ActiveXObject("Msxml2.XMLHTTP");  
		} catch (e) {
			try {
				request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				alert("Il browser non supporta AJAX");
				return null;
			}
		}
	}
	return request;
}

function cercaNazionalita(){
	var input = document.getElementById("nazionalita").value;
	var param = "nazionalita="+input;
	loadAjaxDoc('CercaNazione', "GET", param, handleNAZ);
}

function loadAjaxDoc(url, method, params, cFuction) {
	var request = createXMLHttpRequest();
	if(request){
		request.onreadystatechange = function() {
			if (this.readyState == 4) {
				if (this.status == 200) {
				    cFuction(this);
				} else {				
					if(this.status == 0){ // When aborting the request
						alert("Problemi nell'esecuzione della richiesta: nessuna risposta ricevuta nel tempo limite");
					} else { // Any other situation
						alert("Problemi nell'esecuzione della richiesta:\n" + this.statusText);
					}
					return null;
				}
		    }
		};
		
		setTimeout(function () {     // to abort after 15 sec
        	if (request.readyState < 4) {
            	request.abort();
        	}
    	}, 15000); 
		
		if(method.toLowerCase() == "get"){
			if(params){
				request.open("GET", url + "?" + params, true);
			} else {
				request.open("GET", url, true);
			}
			request.setRequestHeader("Connection", "close");
	        request.send(null);
	        
		} else {
			
			if(params){
				request.open("POST", url, true);
				request.setRequestHeader("Connection", "close");
				request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	        	request.send(params);
			} else {
				console.log("Usa GET se non ci sono parametri!");
				return null;
			}
			
		}
		
	}
}

function handleNAZ(request) {
    var response = JSON.parse(request.responseText);
    var suggerimentiDiv = document.querySelector(".suggerimentiNazione");
    suggerimentiDiv.innerHTML = "";
    
    response.forEach(function(item) {
        var suggerimento = document.createElement("div");
        suggerimento.classList.add("suggerimento");
        suggerimento.innerHTML = item;
        suggerimento.onclick = function() {
            document.getElementById("nazionalita").value = item;
            suggerimentiDiv.innerHTML = "";
        };
        suggerimentiDiv.appendChild(suggerimento);
    });
}


