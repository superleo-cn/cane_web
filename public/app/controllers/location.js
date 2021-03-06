define(['/app/controllers/module.js'], function (controllers) {
	'use strict';
    controllers.controller("Location", function($http, $rootScope, $scope, $translate, $cookies, $stateParams, HttpService, Constants) {
    	// get DateTime yyyyMMddHHmmss
	    var getDateTime = function(){
	    	var dd = new Date()
	    	var val = '' + dd.getFullYear() 
	    				+ (dd.getMonth() < 9 ? '0' + (dd.getMonth()+1) : (dd.getMonth()+1)) 
	    				+ (dd.getDate() < 10 ? '0' + (dd.getDate()) : (dd.getDate())) 
	    				+ (dd.getHours() < 10 ? '0' + dd.getHours() : dd.getHours())
	    				+ (dd.getMinutes() < 10 ? '0' + dd.getMinutes() : dd.getMinutes())
	    				+ (dd.getSeconds() < 10 ? '0' + dd.getSeconds() : dd.getSeconds())
	       return val;
	    }

		$scope.braceletId = "0"; // default bracelet id
		var id = $stateParams.id;

        if(id != null && id != ""){
        	HttpService.url = '/braceletsByUserId/' + id;
     	    HttpService.postParams = {};
          	HttpService.getParams = {};
          	HttpService.get(function(data, status) {
          		if(data){
    				if(data.code == Constants.SUCCESS){
    					$scope.bracelets = data.datas;
    					if($scope.bracelets == null || $scope.bracelets.length == 0){
    		     	    	$scope.bracelets[0] = {"braceletId" : "0", "name" : "You don't have any bracelet"};
    		     	    }else{
    		     	    	$scope.braceletId = $scope.bracelets[0].braceletId;
    		     	    }
    				}else{
    			    	$scope.bracelets[0] = {"braceletId" : "0", "name" : "You don't have any bracelet"};
    				}
    			}
        	});
        }else{
        	$scope.bracelets = jQuery.parseJSON($cookies.current_bracelets);
			if(typeof $scope.bracelets != 'object') {
				$scope.bracelets = jQuery.parseJSON($scope.bracelets);
			}
     	    if($scope.bracelets == null || $scope.bracelets.length == 0){
     	    	$scope.bracelets[0] = {"braceletId" : "0", "name" : "You don't have any bracelet"};
     	    }else{
     	    	$scope.braceletId = $scope.bracelets[0].braceletId;
     	    }
        }
	    
        // Report URL
		function getUrl(flag){
			return '/api/findRealtimeList/' + $scope.braceletId + '/' + getDateTime() + "/" + flag;
		}

		// Google Map Start
		var poly;
		var map;
		var size = 0;

		var directionsService = new google.maps.DirectionsService;
		var directionsDisplay = new google.maps.DirectionsRenderer;

		var flightPlanCoordinates = [
			//new google.maps.LatLng(1.293873, 103.806489),
			new google.maps.LatLng(1.283963, 103.804600),
			new google.maps.LatLng(1.284606, 103.816043),
			new google.maps.LatLng(1.283019, 103.835843),
			new google.maps.LatLng(1.288124, 103.847966),
			new google.maps.LatLng(1.300395, 103.854403),
			new google.maps.LatLng(1.313180, 103.839211),
			new google.maps.LatLng(1.320989, 103.823848),
			new google.maps.LatLng(1.316269, 103.806596),
			new google.maps.LatLng(1.298078, 103.803248)
		];

		var datetimes = [
			'09:02',
			'09:32',
			'10:10',
			'10:15',
			'10:30',
			'12:07',
			'15:30',
			'16:58',
			'20:00',
			'21:50'
		];

		function initialize() {
			var mapOptions = {
				zoom: 14,
				center: new google.maps.LatLng(1.293873, 103.806489),
				mapTypeId: google.maps.MapTypeId.ROADMAP
			};

			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
			$scope.map = map;

			var polyOptions = {
				strokeColor: '#0ABAF5',
				strokeOpacity: 1.0,
				strokeWeight: 5
			};
			//poly = new google.maps.Polyline(polyOptions);
			//poly.setMap(map);

			// Add a listener for the click event
			//google.maps.event.addListener(map, 'click', addLatLng);

			directionsDisplay.setMap(map);
			showMaps();

			/*
			var myRoute = setInterval(
				function(){
					if(size < flightPlanCoordinates.length){
						if(size == 0){
							addLatLng('start_icon.png');
						} else if(size == flightPlanCoordinates.length - 1) {
							addLatLng('end_icon.png');
						} else {
							addLatLng('user_walk.png');
						}
					}else{
						clearInterval(myRoute);
					}
				}, 3000);
			*/

		}

		/**
		 * Handles click events on a map, and adds a new point to the Polyline.
		 * @param {google.maps.MouseEvent} event
		 */
		function addLatLng(pic) {
			var path = poly.getPath();
			// Because path is an MVCArray, we can simply append a new coordinate
			// and it will automatically appear.
			path.push(flightPlanCoordinates[size]);

			// Add a new marker at the new plotted point on the polyline.
			var marker = new google.maps.Marker({
				position: flightPlanCoordinates[size],
				title: datetimes[size],
				icon: '/img/' + pic,
				map: map
			});

			size++;
		}

		function showMaps() {
			var waypts = [];
			for (var i = 1; i < flightPlanCoordinates.length; i++) {
				waypts.push({
					location: flightPlanCoordinates[i],
					stopover: true
				});
			}

			directionsService.route({
				origin: flightPlanCoordinates[size],
				destination: flightPlanCoordinates[flightPlanCoordinates.length-1],
				waypoints: waypts,
				optimizeWaypoints: true,
				travelMode: google.maps.TravelMode.DRIVING
			}, function(response, status) {
				if (status === google.maps.DirectionsStatus.OK) {
					directionsDisplay.setDirections(response);
					//var route = response.routes[0];
					//var summaryPanel = document.getElementById('directions_panel');
					//summaryPanel.innerHTML = '';
					//// For each route, display summary information.
					//for (var i = 0; i < route.legs.length; i++) {
					//	var routeSegment = i + 1;
					//	summaryPanel.innerHTML += '<b>Route Segment: ' + routeSegment +
					//		'</b><br>';
					//	summaryPanel.innerHTML += route.legs[i].start_address + ' to ';
					//	summaryPanel.innerHTML += route.legs[i].end_address + '<br>';
					//	summaryPanel.innerHTML += route.legs[i].distance.text + '<br><br>';
					//}
				} else {
					window.alert('Directions request failed due to ' + status);
				}
			});
		}

		initialize();

	})
});
