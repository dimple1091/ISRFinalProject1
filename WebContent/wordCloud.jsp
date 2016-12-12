<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://d3js.org/d3.v4.min.js"></script>

<script src="d3.layout.cloud.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
.bar {
  fill: steelblue;
}

.bar:hover {
  fill: brown;
}

.axis--x path {
  display: none;
}
 

body {
    font-family:"Lucida Grande","Droid Sans",Arial,Helvetica,sans-serif;
}
.legend {
    border: 1px solid #555555;
    border-radius: 5px 5px 5px 5px;
    font-size: 0.8em;
    margin: 10px;
    padding: 8px;
}
.bld {
    font-weight: bold;
}
</style>
</head>
<body onload="callServletMain()">


<script>

//var json={"Has TV":"true","lunch":"true","city":"Gilbert","latitude":"33.363016","hipster":"false","review_count":"10","Alcohol":"none","full_address":"721 N Arizona Ave\nGilbert, AZ 85233","type":"business","classy":"false","lot":"true","Caters":"true","validated":"false","trendy":"false","Price Range":"2","romantic":"false","upscale":"false","street":"false","casual":"true","Good for Kids":"true","Outdoor Seating":"false","categories":"Restaurants","state":"AZ","Takes Reservations":"false","longitude":"-111.841114","Waiter Service":"false","garage":"false","intimate":"false","stars":"3.0","dinner":"true","Accepts Credit Cards":"true","latenight":"false","Good For Groups":"true","Noise Level":"average","Take-out":"true","dessert":"false","Wheelchair Accessible":"true","brunch":"false","divey":"false","name":"QQ Grill","Attire":"casual","Delivery":"false","breakfast":"false","valet":"false","touristy":"false","business_id":"7UZQ6qGX8lP7a3jokfYVjQ","open":"false"}
var reviews="Mr Hoagie is an institution. Walking in, it does seem like a throwback to 30 years ago, old fashioned menu board, booths out of the 70s, and a large selection of food. Their speciality is the Italian Hoagie, and it is voted the best in the area year after year. I usually order the burger, while the patties are obviously cooked from frozen, all of the other ingredients are very fresh. Overall, its a good alternative to Subway, which is down the road Excellent food. Superb customer service. I miss the mario machines they used to have, but it's still a great place steeped in tradition Yes this place is a little out dated and not opened on the weekend. But other than that the staff is always pleasant and fast to make your order. Which is always spot on fresh veggies on their hoggies and other food. They also have daily specials and ice cream which is really good. I had a banana split they piled the toppings on. They win pennysaver awards ever years i see why. All the food is great here. But the best thing they have is their wings. Their wings are simply fantastic!!  The \"Wet Cajun\" are by the best & most popular.  I also like the seasoned salt wings.  Wing Night is Monday & Wednesday night, $0.75 whole wings!\n\nThe dining area is nice. Very family friendly! The bar is very nice is well.  This place is truly a Yinzer's dream!!  \"Pittsburgh Dad\" would love this place n'at! We checked this place out this past Monday for their wing night. We have heard that their wings are great and decided it was finally time to check it out. Their wings are whole wings and crispy, which is a nice change of pace. I got their wet Cajun sauce and garlic butter wings. The Cajun did not have a bold enough flavor for me and their sauce is too thin. The sauce was also thin for the garlic butter, but that is more expected. They were better than average, but I don't like seeing all the sauce resting at the bottom of the boat. I would definitely come try this place out again to sample some of the other items on the menu, but this will probably not become a regular stop for wings anytime soon. Wing sauce is like water. Pretty much a lot of butter and some hot sauce (franks red hot maybe).  The whole wings are good size and crispy, but for $1 a wing the sauce could be better. The hot and extra hot are about the same flavor/heat.  The fish sandwich is good and is a large portion, sides are decent.";
var counts="";

function callServlet() {
	callServletMain();
	callServletSearch();
}

function callServletMain() {
	  
	   // var query = document.getElementById("query").value;
			//alert(query);
	    $.ajax({
	        type: "GET",
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
	         //  counts= wordcount(json);
	        }
	    });
	}

	function callBackFunc(jsonData){
		  
		var counts=wordcount(jsonData);  
		var frequency=[];
	    
	    var wordcountarray = [];
		for(var key in counts){
			if (counts.hasOwnProperty(key)) {
				wordcountarray.push({"text" : key, "size" : (counts[key])*5})
	    	  }
			
			
		}
		var frequency_list = wordcountarray;
		

	    var color = d3.scaleLinear()
	            .domain([0,1,2,3,4,5,6,10,15,20,100])
	            .range(["#ddd", "#ccc", "#bbb", "#aaa", "#999", "#888", "#777", "#666", "#555", "#444", "#333", "#222"]);

	    d3.layout.cloud().size([800, 300])
	            .words(frequency_list)
	            .rotate(0)
	            .fontSize(function(d) { return d.size; })
	            .on("end", draw)
	            .start();

	    function draw(words) {
	        d3.select("body").append("svg")
	                .attr("width", 850)
	                .attr("height", 350)
	                .attr("class", "wordcloud")
	                .append("g")
	                // without the transform, words words would get cutoff to the left and top, they would
	                // appear outside of the SVG area
	                .attr("transform", "translate(320,200)")
	                .selectAll("text")
	                .data(words)
	                .enter().append("text")
	                .style("font-size", function(d) { return d.size + "px"; })
	                .style("fill", function(d, i) { return color(i); })
	                .attr("transform", function(d) {
	                    return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
	                })
	                .text(function(d) { return d.text; });
	    }

	
	}


	function wordcount(value) {

    /* Below is a regular expression that finds alphanumeric characters
       Next is a string that could easily be replaced with a reference to a form control
       Lastly, we have an array that will hold any words matching our pattern */
    var pattern = /\w+/g,
        //string = "I I am am am yes yes.",
        
    string = value;
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
   
    return counts;
}

	function callServletSearch() {
		  
		   // var query = document.getElementById("query").value;
				//alert(query);
		    $.ajax({
		        type: "POST",
		        url: "ReviewSearchServlet", //&quot;/AjaxServletCalculator&quot;,
		        data: {
		            "business_id": query
		        },
		        //contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		        //contentType: 'text/html; charset=utf-8',
		        dataType: "json",
		        //if received a response from the server
		        success: function(json) {
		            console.log(JSON.stringify(json));
		            //alert(JSON.stringify(json));
		            callBackFunc1(json);
		         //  counts= wordcount(json);
		        }
		    });
		}

		function callBackFunc1(jsonData){
			  
			var counts=wordcount(jsonData);  
			var frequency=[];
			var svg = d3.select("svg"),
		    margin = {top: 20, right: 20, bottom: 30, left: 40},
		    width = +svg.attr("width") - margin.left - margin.right,
		    height = +svg.attr("height") - margin.top - margin.bottom;

		var x = d3.scaleBand().rangeRound([0, width]).padding(0.1),
		    y = d3.scaleLinear().rangeRound([height, 0]);

		var g = svg.append("g")
		    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

		d3.tsv("data.tsv", function(d) {
		  d.frequency = +d.frequency;
		  return d;
		}, function(error, data) {
		  if (error) throw error;

		  x.domain(data.map(function(d) { return d.letter; }));
		  y.domain([0, d3.max(data, function(d) { return d.frequency; })]);

		  g.append("g")
		      .attr("class", "axis axis--x")
		      .attr("transform", "translate(0," + height + ")")
		      .call(d3.axisBottom(x));

		  g.append("g")
		      .attr("class", "axis axis--y")
		      .call(d3.axisLeft(y).ticks(10, "%"))
		    .append("text")
		      .attr("transform", "rotate(-90)")
		      .attr("y", 6)
		      .attr("dy", "0.71em")
		      .attr("text-anchor", "end")
		      .text("Frequency");

		  g.selectAll(".bar")
		    .data(data)
		    .enter().append("rect")
		      .attr("class", "bar")
		      .attr("x", function(d) { return x(d.letter); })
		      .attr("y", function(d) { return y(d.frequency); })
		      .attr("width", x.bandwidth())
		      .attr("height", function(d) { return height - y(d.frequency); });
		});   
		   
			

		}
  
</script>

<div id="barchart">
	<svg width="300" height="400"></svg>
</div>




</body>
</html>