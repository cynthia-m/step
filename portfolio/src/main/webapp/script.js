// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */

var absoluteIndex = 0;
//absoluteIndex is the index of the picture

function loadIntro(){
  window.location =("./intro.html");
}

function getPhotos_pre(){
  
  window.location=("./photography.html");
    
  getPhotos(0);
}


function getPhotos(moveDir){
  if(moveDir==1){
    //move to next image
    absoluteIndex++;
  }
  else if (moveDir==-1){
    //move to prev image
    absoluteIndex--;
  }
    
  var picsLen = document.getElementsByClassName("pics").length
  var pics = document.getElementsByClassName("pics");
  for(var currIndex =0; currIndex<picsLen; currIndex++){
    if(currIndex!=absoluteIndex%picsLen){
      //currIndex is the index of the image in the array
      (pics[currIndex]).classList.add("hidden");
    }
    else{
      (pics[currIndex]).classList.remove("hidden");
    }
  }
  
  if(absoluteIndex>0){
    document.getElementById("prev").style.display="block";
  }
  else{
    document.getElementById("prev").style.display="none";
  }
	if(absoluteIndex<picsLen-1){
		document.getElementById('next').style.display = "block";
  }
  else{
    document.getElementById("next").style.display="none";
  }
  document.getElementById('test').innerText = absoluteIndex;
}

function gotoComments(){
  window.location=("./get-comments.html");
}

function getComments(){
  var displayNumComments = document.getElementById("quantity").value;
  fetch('/data?max='+displayNumComments).then(response => response.text()).then((quote) => {
    document.getElementById('comments').innerText = quote;
  });
}

function submitComments(){
  window.location="./comments.html";
  hideForm();
}

function deleteComments(){
  fetch(new Request('/delete-data', {method: 'POST'})).then( 
    //delete data
    fetch(new Request('/data', {method: 'POST'}))).then(
      //make sure comments updates
      getComments()
    );
}

function hideForm(){
  fetch('/login-status').then(response=>checkLogIn(response));
}

function checkLogIn(b){
  if(b.equals("True")){
    document.getElementById("form").classList.remove("hidden");
  }
  else{
     document.getElementById("form").classList.add("hidden");
  }
}

