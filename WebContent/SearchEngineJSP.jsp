<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <head>
      <meta name="generator" content="HTML Tidy for HTML5 (experimental) for Windows https://github.com/w3c/tidy-html5/tree/c63cc39" />
      <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
      <title>Grand Responsive Template</title>
      <meta name="keywords" content="" />
      <meta name="description" content="" />
      <meta charset="utf-8" />
      <meta name="viewport" content="initial-scale=1" />
      <link href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,700italic,400,600,700,800' rel='stylesheet' type='text/css' />
      <link rel="stylesheet" href="css/font-awesome.min.css" />
      <link rel="stylesheet" href="css/bootstrap.min.css" />
      <link rel="stylesheet" href="css/templatemo_misc.css" />
      <link rel="stylesheet" href="css/jquery-ui.css" />
      <!-- popular tab -->
      <link rel="stylesheet" href="css/templatemo_style.css" />
      <link rel="stylesheet" type="text/css" href="css/SearchEngine.css" />
      <script src="js/jquery-1.11.2.min.js"></script>
      <!-- lightbox -->
      <script src="js/templatemo_tab.js"></script>
      <!-- lightbox -->
      <script src="js/bootstrap-collapse.js"></script>
      <script src="js/jquery-ui.min.js"></script>
      <!-- popular tab -->
      <script src="js/json2.js"></script>
     <!--  <script src="js/d3.v3.min.js"></script> -->
     <!--  <script src="js/d3.layout.cloud.js"></script> -->
     <script src="js/d3.v3.min.js"></script>
     <script src="js/d3.layout.cloud.js"></script>
     <script src="js/easy/jquery.easy-autocomplete.min.js"></script> 

<!-- CSS file -->
<!-- <link rel="stylesheet" href="js/easy/easy-autocomplete.min.css">  -->
<!-- Additional CSS Themes file - not required-->
<!-- <link rel="stylesheet" href="js/easy/easy-autocomplete.themes.min.css">  -->

<style>

  .nav3 {
    height: auto;
    width: auto;
    float: right;
    padding-left: 20px;
    font-family: Arial, Helvetica, sans-serif;
    font-size: 12px;
    color: #333333;
    magin-top: 0;
	vertical-align : top;
     display:inline-block;
     position : relative;
     top : -80px;
 }
.images_icon{
	display:inline-block;
	margin-right: 6px;
	
	max-width:50%;
  	max-height:50%;
 }

</style>

   </head>
   <body>
   <!-- <form action="">
   <input id="query2" />
   </form> -->
   <div id="wordclouddiv"></div>
      <form class="form-wrapper cf" style="margin-left:30px" onkeypress="return event.keyCode != 13;">
         <input style="height:39px;background:white;color:black" type="text" placeholder="Search here..." id="query" required/> 

         <input style="height:39px;background:white;color:black" type="hidden" id="text" value="How are you How are you How are you How are you"/>
          
         <button type="button" id="buttonId" onclick="callServlet()">Search</button>
      </form>

      <div id="searchResults">
      
	    <div class="col-md-8 margin30">
	         <div class="templatemo_homemid_right shadow">
	            <div class="box">
	               <div class="icon">
	                  <img style="margin-top:0" src="images/logo.jpg" alt="templatemo home icon 1" />
	               </div>
	               <div class="text">
	                <a href="">  <div class="title" style="font-family:serif;color:#4C4CFF">
	                    <b>1. Arsenal Cider House &amp; Wine Cellar</b>
	                  </div></a>
	                  <img src="images/stars.png" />
	                  <img src="images/stars.png" />
	                  <img src="images/stars.png" />
	                  <img src="images/stars.png" />164 reviews
	                  <p>The growlers are such a great deal, get one and a few glasses and enjoy.Everything on the menu is a family recipe.
	                     These are some of the nicest people I&#39;ve ever met in my life. The friendliest small business owners I think I&#39;ve
	                     ever encountered. Genuinely happy, kind, friendly and eager to explain everything on the menu.
	                  </p>
	               </div>
	               <div class="clear"></div>
	            </div>
	         </div>
	      </div>

      </div>
      
      
      <div class="container">
         <div class="clear"></div>
      </div>
      <!-- home end -->
      <!-- footer start -->
      <div class="container">
         <div class="row">
            <div class="col-md-12 margin30 center"></div>
            <div class="footermargin">&#160;</div>
         </div>
      </div>
      <!-- footer end-->
      <script>
      
      var options = {

    		  url: "yelp.json",

    		  getValue: "name",
    

    		  list: {	
    		    match: {
    		      enabled: true
    		    }
    		  },
    		  template: {
    				type: "custom",
    				method: function(value, item) {
    					return value + "abc";
    				}
    			}
    		  //requestDelay: 5000,
    		 // theme: "square"
    		};

    		//$("#query").easyAutocomplete(options);
    	
   /* 	$(document).ready(function () {
    	    
    	    $("#query").autocomplete({
    	        delay: 100,
    	        source: function (request, response) {
    	            
    	            // Suggest URL
    	            var suggestURL = "http://suggestqueries.google.com/complete/search?client=chrome&q=%QUERY";
    	            suggestURL = suggestURL.replace('%QUERY', request.term);
    	            
    	            // JSONP Request
    	            $.ajax({
    	                method: 'GET',
    	                dataType: 'jsonp',
    	                jsonpCallback: 'jsonCallback',
    	                url: suggestURL
    	            })
    	            .success(function(data){
    	            
    	                response(data[1]);
    	             //   var dataList = document.getElementById('json-datalist');
    	                var dropDownData = data[1];
    	           	// var option = document.createElement('option');
 	                // Set the value using the item in the JSON array.
 	               // option.value = "hi";
 	                // Add the <option> element to the <datalist>.
 	               // dataList.appendChild(option); 
    	                
    	            });
    	        }
    	    });

    	});
     */
      
      

      function callServlet() {
    	  
          var query = document.getElementById("query").value;
//alert(query);
          $.ajax({
              type: "POST",
              url: "SearchEngine", //&quot;/AjaxServletCalculator&quot;,
              data: {
                  "query": query
              },
              //contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
              //contentType: 'text/html; charset=utf-8',
              dataType: "json",
              //if received a response from the server
              success: function(json) {
                  console.log(JSON.stringify(json));
                  //alert(JSON.stringify(json));
                  callBackFunc(json);
              }
          });
      }
      
      
      function callBackFunc(jsonData){
    	  
    	  var divTag = document.getElementById("searchResults");	
    	  divTag.innerHTML = "";
    	var results = jsonData.result;
    	//alert(results.length);
    	for(i = 0; i < results.length; i++){
    		var place = results[i];
    		var name = place.name;
    		var review_count = place.review_count;
    		var stars = place.stars;
	//alert(place.tips);
  	      var divElement = "<div class=\"col-md-8 margin30\">";
  	    	divElement +=  		"<div class=\"templatemo_homemid_right shadow\">";
  	    	divElement +=  			 "<div class=\"box\">";
  	    	//divElement +=   			 "<div class=\"icon\">";
  	    	//divElement +=  					 "<img style=\"margin-top:0\" src=\"images/logo.jpg\" alt=\"templatemo home icon 1\" />";
  	    	//divElement += 			      "</div>";
  	    	divElement +=       		  "<div class=\"text\">";
  	    	divElement +=       		  		"<a href=\"javascript:openNewPage()\"><div class=\"title\" style=\"font-family:serif;color:#4C4CFF\">";
  	    	divElement +=            	   			"<b>"+name+"</b>";
  	    	divElement +=		         		 "</div></a>";
  	    	for(j=0; j < parseInt(stars); j++){
  	    		divElement +=  "<img src=\"images/stars.png\" />";
  	    	}
  	    	//divElement +=          		  		 "<img src=\"images/stars.png\" />";
  	    	//divElement +=          		 		 "<img src=\"images/stars.png\" />";
  	    	//divElement +=          		  		 "<img src=\"images/stars.png\" />";
  	    	//divElement +=          		  		 "<img src=\"images/stars.png\" />";
  	    	divElement +=          		  	" "+review_count + " reviews";
  	    	divElement +=		          		 "<p>"+place.full_address+"</p>";
  	    	divElement +=		          		 "<p> Price Range : "+"$".repeat(place["Price Range"])+"</p>";
  	    	divElement +=          		  "</div>";
 	    //	divElement +=   			 "<div  class=\"nav3\">";
  	    	//divElement +=  					 "<img style=\"width:100px;height:100px;\" class=\"images_icon\"  src=\"yelp/_i6oyu8cWLCTJNICHmyqDg.jpg\" alt=\"templatemo home icon 1\" />";
  	    	//divElement +=  					 "<img style=\"width:100px;height:100px;\" class=\"images_icon\"  src=\"yelp/_i6oyu8cWLCTJNICHmyqDg.jpg\" alt=\"templatemo home icon 1\" />";
  	    	//divElement +=  					 "<img style=\"width:100px;height:100px;\" class=\"images_icon\" src=\"yelp/_IT8JEaPKpmnvMW_QMlVdA.jpg\" alt=\"templatemo home icon 1\" />";
  	    	//divElement +=  					 "<img style=\"width:100px;height:100px;\" class=\"images_icon\" src=\"yelp/_j_bQytGo3Y8_9d5Tpb2VA.jpg\" alt=\"templatemo home icon 1\" />";
  	    	//divElement += 			      "</div>";
  	    	divElement +=       		  "<div class=\"clear\"></div>";
 
  	    	divElement += 			"</div>";
  	    	divElement +=		"</div>";
  	    	divElement += "</div>";
    		
  	    	divTag.innerHTML  += divElement;	
  	    	
    	  }
      }
      
      function openNewPage(){
    	  window.open("subsection.jsp");
      }
      
      var area = document.getElementById('text');
      function wordcount() {

          /* Below is a regular expression that finds alphanumeric characters
             Next is a string that could easily be replaced with a reference to a form control
             Lastly, we have an array that will hold any words matching our pattern */
          var pattern = /\w+/g,
              //string = "I I am am am yes yes.",
              
              string = area.value;
          matchedWords = string.match(pattern);

          /* The Array.prototype.reduce method assists us in producing a single value from an
             array. In this case, we're going to use it to output an object with results. */

          var counts = matchedWords.reduce(function(stats, word) {

              /* `stats` is the object that we'll be building up over time.
                 `word` is each individual entry in the `matchedWords` array */
              if (stats.hasOwnProperty(word)) {
                  /* `stats` already has an entry for the current `word`.
                     As a result, let's increment the count for that `word`. */
                  stats[word] = stats[word] + 1;
              } else {
                  /* `stats` does not yet have an entry for the current `word`.
                     As a result, let's add a new entry, and set count to 1. */
                  stats[word] = 1;
              }

              //	wordcountarray.push({"text" : word, "size" : stats[word]});

              /* Because we are building up `stats` over numerous iterations,
                 we need to return it for the next pass to modify it. */
              return stats;

          }, {});

          //alert(JSON.stringify(wordcountarray));
          /* Now that `counts` has our object, we can log it. */
          console.log(counts);
          return counts;
      }

      var counts = wordcount();
     // alert(JSON.stringify(counts));

    /*  var wordcountarray = [{
          "text": "How",
          "size": 90
      }, {
          "text": "are",
          "size": 90
      }, {
          "text": "you",
          "size": 90
      }];*/
      
     var wordcountarray = [];
      
      for(key in counts){
   		wordcountarray.push({"text" : key, "size" : (counts[key])*10})
   	  }
      
      
      var fill = d3.scale.category20();


      var layout = d3.layout.cloud()
          .size([500, 500])
          /* .words([
             "Hello", "world", "normally", "you", "want", "more", "words",
             "than", "this"].map(function(d) {
             return {"text": d, "size": 10 + Math.random() * 90};
           }))*/
          .words(wordcountarray)
          .padding(5)
          .rotate(function() {
              return ~~(Math.random() * 2) * 90;
          })
          .font("Impact")
          .fontSize(function(d) {
              return d.size;
          })
          .on("end", draw);

     // layout.start();

      function draw(words) {
          d3.select("#wordclouddiv").append("svg")
              .attr("width", layout.size()[0])
              .attr("height", layout.size()[1])
              .append("g")
              .attr("transform", "translate(" + (layout.size()[0] / 2) + "," + (layout.size()[1] / 2) + ")")
              .selectAll("text")
              .data(words)
              .enter().append("text")
              .style("font-size", function(d) {
                  return d.size + "px";
              })
              .style("font-family", "Impact")
              .style("fill", function(d, i) {
                  return fill(i);
              })
              .attr("text-anchor", "middle")
              .attr("transform", function(d) {
                  return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
              })
              .text(function(d) {
                  return d.text;
              });
       }
	      
  
    	    /*$(function () {
                 
                      $(&#39;#buttonId&#39;).click(function() {
                              alert(&quot;button&quot;);
                      callServlet();
                  });  
         });*/
         
         
         function initGeolocation()
         {
            if( navigator.geolocation )
            {
               // Call getCurrentPosition with success and failure callbacks
               navigator.geolocation.getCurrentPosition( success, fail );
            }
            else
            {
               alert("Sorry, your browser does not support geolocation services.");
            }
         }

         function success(position)
         {
			//alert(position.coords.longitude+" , "+position.coords.latitude);
             //document.getElementById('long').value = position.coords.longitude;
            // document.getElementById('lat').value = position.coords.latitude
         }

         function fail()
         {
        	 alert("cannot");
            // Could not obtain location
         }
         
         
         $("#query").keypress(function(event){
        	    if(event.keyCode == 13){
        	    	//document.getElementById("query").value = this.value;
        	    	//alert(document.getElementById("query").value);
        	        $("#buttonId").click();
        	    }
        	});
         initGeolocation();
      </script>
   </body>
</html>