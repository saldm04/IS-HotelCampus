function successivo(){
	let items = document.querySelectorAll('.item')
    document.querySelector('.slide').appendChild(items[0])
} 

function precedente(){
	 let items = document.querySelectorAll('.item')
	 document.querySelector('.slide').prepend(items[items.length - 1])
}