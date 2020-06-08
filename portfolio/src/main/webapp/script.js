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

var absolute_index = 0;
//absolute_index is the index of the picture

function loadIntro(){
    window.location =("/intro.html");
}

function getPhotos_pre(){
    window.location=("./photography.html");
    getPhotos(0);
}

function getPhotos(move_dir){
  
  if(move_dir==1){
    //move to next image
    absolute_index++;
  }
  else if (move_dir==-1){
    //move to prev image
    absolute_index--;
  }
    
  var pics_len = document.getElementsByClassName("pics").length
  var pics = document.getElementsByClassName("pics");
  for(var curr_index =0; curr_index<pics_len; curr_index++){
    
    if(curr_index!=absolute_index%pics_len){
      //curr_index is the index of the image in the array
      
      (pics[curr_index]).classList.add("hidden");
    }
    else{
      (pics[curr_index]).classList.remove("hidden");
      
    }
  }
  
  // document.getElementById("test").innerText = absolute_index;
  if(absolute_index>0){
    document.getElementById("prev").style.display="block";
  }
  else{
    document.getElementById("prev").style.display="none";
  }
	if(absolute_index<pics_len-1){
		document.getElementById('next').style.display = "block";
  }
  else{
    document.getElementById("next").style.display="none";
  }
  // alert(pic  s_len);
  document.getElementById('test').innerText = absolute_index;
}


function getComments(){
  window.location=("./get-comments.html");
  fetch('/data').then(response => response.text()).then((quote) => {
    document.getElementById('why').innerText = quote;
  });
}

function submitComments(){
  window.location="/comments.html";
}


function deleteComments(){
  fetch(new Request('/delete-data', {method: 'POST'})).then( 
    fetch(new Request('/data', {method: 'POST'}))).then(
      getComments()
    );
}