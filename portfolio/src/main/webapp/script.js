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
  } else if (moveDir==-1){
    //move to prev image
    absoluteIndex--;
  }
  var picsLen = document.getElementsByClassName("pics").length
  var pics = document.getElementsByClassName("pics");
  for(var currIndex =0; currIndex<picsLen; currIndex++){
    if(currIndex!=absoluteIndex%picsLen){
      //currIndex is the index of the image in the array
      (pics[currIndex]).classList.add("hidden");
    } else{
      (pics[currIndex]).classList.remove("hidden");
    }
  }
  
  if(absoluteIndex>0){
    document.getElementById("prev").style.display="block";
  } else{
    document.getElementById("prev").style.display="none";
  }
	if(absoluteIndex<picsLen-1){
		document.getElementById('next').style.display = "block";
  } else{
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

function deleteComments(){
  fetch(new Request('/delete-data', {method: 'POST'})).then( 
    //delete data
    fetch(new Request('/data', {method: 'POST'}))
  ).then(
      //make sure comments updates
      getComments()
  );
}

function submitComments(){
  window.location="./comments.html";
}

function hideForm(){
  fetch('/login-status').then(response => response.json()).then((result) =>checkLogIn(result));
}

function checkLogIn(b){
  alert(typeof(b.result));
  alert(b.result);
  if(!b.result){
    alert("not logged in");
    document.getElementById("formuwu").classList.add("hidden");
    document.getElementById("formuwu").classList.remove("notHidden");
  } else{
    alert("logged in");
    document.getElementById("formuwu").classList.remove("hidden");
    document.getElementById("formuwu").classList.add("notHidden");
  }
}

google.charts.load('current', {
  'packages':['geochart','corechart'],
  'mapsApiKey': 'AIzaSyAOz5UekvNaqSJ06dcAMHfCOS-F8A9fivg'
  }
);

function getLocChart() {
  window.location = "./chart.html";
}

function getVisitedCountryCountChart() {
  var data = google.visualization.arrayToDataTable([
    ['Country', 'Times Visited'],
    ['China', 19],
    ['France', 1],
    ['Japan', 1],
    ['South Korea', 1],
    ['Amsterdam', 1],
    ['Spain', 1],
    ['England', 1],
    ['Canada', 7]
  ]);

  var options = {};
  const chart = new google.visualization.GeoChart(document.getElementById('jump'));
  chart.draw(data, options);
}

function getFlowerChart() {
  window.location = "./favFlower.html";
}

function flowerChart() {
fetch('/fav-flower').then(response => response.json()).then(
  (result) =>{
   const data = new google.visualization.DataTable();
    data.addColumn('string', 'Flower');
    data.addColumn('number', 'Votes');
    //fix variable name
    var yeet = Object.keys(result);
    var i;
    for(i = 0; i<yeet.length;i++){
      data.addRow([yeet[i], result[yeet[i]]]);
    }
    const options = {
      'title': 'Favorite Flowers',
      'width':600,
      'height':500
    };
    const chart = new google.visualization.ColumnChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
  });
}

function gotoMap(){
  window.location = "./map.html";
}

function createMap() {
  const map = new google.maps.Map(
    document.getElementById('map'),
    {center: {lat: 37.422, lng: -122.084}, zoom: 16}
  );
}