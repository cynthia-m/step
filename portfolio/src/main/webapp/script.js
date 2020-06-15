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

function loadIntro() {
  window.location = "./intro.html";
}

function getPhotos_pre() {
  window.location = "./photography.html";  
  getPhotos(0);
}
var absoluteIndex = 0;
//absoluteIndex is the index of the picture
function getPhotos(moveDir) {
  if (moveDir == 1) {
    //move to next image
    absoluteIndex++;
  } else if (moveDir == -1) {
    //move to prev image
    absoluteIndex--;
  }
  var picsLen = document.getElementsByClassName("pics").length
  var pics = document.getElementsByClassName("pics");
  for (var currIndex = 0; currIndex < picsLen; currIndex++) {
    if (currIndex != absoluteIndex) {
      //currIndex is the index of the image in the array
      (pics[currIndex]).classList.add("hidden");
    } else {
      (pics[currIndex]).classList.remove("hidden");
    }
  }
  //control when prev and next buttons show up
  //FIX THISSSS
  if (absoluteIndex > 0){
    document.getElementById("form").classList.remove("hidden");
    document.getElementById("form").classList.add("notHidden");
  } else {
    document.getElementById("form").classList.add("hidden");
    document.getElementById("form").classList.remove("notHidden");
  }
	if (absoluteIndex < picsLen-1) {
		document.getElementById("form").classList.remove("hidden");
    document.getElementById("form").classList.add("notHidden");
  } else {
    document.getElementById("form").classList.add("hidden");
    document.getElementById("form").classList.remove("notHidden");
  }
}

function gotoComments() {
  window.location=("./get-comments.html");
}

function getComments() {
  var displayNumComments = document.getElementById("quantity").value;
  fetch('/data?max=' + displayNumComments).then(response => response.text()).then((listOfComments) => {
    document.getElementById('comments').innerText = listOfComments;
  });
}

function deleteComments() {
  fetch(new Request('/delete-data', {method: 'POST'})).then( 
    //delete data
    fetch(new Request('/data', {method: 'POST'}))
  ).then(
      //make sure comments updates
      fetch('/delete-data').then(response => response.text()).then(listOfComments => {
        document.getElementById('comments').innerText = listOfComments;
      }
    ));
}

function submitComments() {
  window.location = "./submitComments.html";
}

function hideForm() {
  fetch('/login-status').then(response => response.json()).then((result) => checkLogIn(result));
}

function checkLogIn(user) {
  if (user.checkLoggedIn == "false") {
    document.getElementById("form").classList.add("hidden");
    document.getElementById("form").classList.remove("notHidden");
    document.getElementById("unique").innerHTML = user.loginURL;
  } else {
    document.getElementById("form").classList.remove("hidden");
    document.getElementById("form").classList.add("notHidden");
    document.getElementById("unique").innerHTML = user.logoutURL;
  }
}

google.charts.load('current', {
  'packages':['geochart','corechart'],
  'mapsApiKey': 'AIzaSyAOz5UekvNaqSJ06dcAMHfCOS-F8A9fivg'
  }
);

function getLocChart() {
  window.location = "./visitedCountryChart.html";
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
    (flowerJson) => {
      const data = new google.visualization.DataTable();
      data.addColumn('string', 'Flower');
      data.addColumn('number', 'Votes');

      var flowerJsonKeys = Object.keys(flowerJson);
      var currKey;
      for(currKey = 0; currKey < flowerJsonKeys.length; currKey++){
        data.addRow([flowerJsonKeys[currKey], flowerJson[flowerJsonKeys[currKey]]]);
      }
      const options = {
        'title': 'Favorite Flowers',
        'width':600,
        'height':500
      };
      const chart = new google.visualization.ColumnChart(
        document.getElementById('chart-container')
      );
      chart.draw(data, options);
    }
  );
}

function gotoMap() {
  window.location = "./map.html";
}

function createMap() {
  const map = new google.maps.Map(
    document.getElementById('map'),
    {center: {lat: 37.422, lng: -122.084}, zoom: 16}
  );
}