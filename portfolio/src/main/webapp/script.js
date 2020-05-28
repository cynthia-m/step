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



function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function loadIntro(){
    document.getElementById("play_button").style.visibility = "hidden"
    //const info = "Hi I'm Cynthia. I'm a sophomore studying Computer Science and Statistics at Cornell University. I like photography and playing tennis and traveling. I was born and raised in New York and spend most of my time taking pictures, hanging out with my friends, or learning new things. I like to read and do my best to keep reading even when life gets hectic." ;
    document.getElementById('introTitle').style.display = "block";
    document.getElementById('introInfo').style.display = "block";
    document.getElementById('profesh1').style.display = "block";
    document.getElementById('profesh2').style.display = "block";
    document.getElementById('profesh3').style.display = "block";
    var pics_len = document.getElementsByClassName("pics").length
    var pics = document.getElementsByClassName("pics");
    for(var i =0; i<pics_len; i++){
        if(i!=1){
        (pics[i]).style.display = "none";
        }
    }
    pics[1].style.visibility="hidden";
    document.getElementById('profesh4').style.display = "none";


}


function getPhotos(){
    const elts = ["introTitle", "introInfo", "profesh1", "profesh2", "profesh3"];
    const len = elts.length;
    for(var i =0; i<len; i++){
        document.getElementById(elts[i]).style.display = "none";
    }
    var pics_len = document.getElementsByClassName("pics").length
    var pics = document.getElementsByClassName("pics");
    for(var i =0; i<pics_len; i++){
        if(i!=1){
        (pics[i]).style.display = "block";
        }
    }
    pics[1].style.visibility="visible";
   // document.getElementById("back").style.display="block";

    document.getElementById('profesh4').style.display = "block";

}

