<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <title>Word Cloud</title>
    <script src="http://d3js.org/d3.v3.min.js"></script>
    <script src="js/d3.layout.cloud.js"></script>
    <script src="js/jquery-1.11.2.min.js"></script>
    <script src="js/jquery-ui.min.js"></script>
    <script src="js/json2.js"></script>
    <script src="http://labratrevenge.com/d3-tip/javascripts/d3.tip.v0.6.3.js"></script>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</head>
<style>

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
    
    .axis {
	  font: 10px sans-serif;
	}

	.axis path,
	.axis line {
	  fill: none;
	  stroke: #000;
	  shape-rendering: crispEdges;
	}
	
	#xaxis .domain {
		fill:none;
		stroke:#000;
	}
	#xaxis text, #yaxis text {
		font-size: 12px;
	}
	
</style>
<body>

	<div class="container-fluid" id="word">
		
	</div>
	<div class="container-fluid" id="bar">
	</div>
	


<!-- <div id="reviews" style="display:block;float:left;margin-top:-200px;">
	<p style="font-size:30px;font-family:Lucida Sans Unicode;color:blue">Reviews</p>
	
</div>
 -->
</body>
<script>


//var query={"Has TV":"true","lunch":"true","city":"Gilbert","latitude":"33.363016","hipster":"false","review_count":"10","Alcohol":"none","full_address":"721 N Arizona Ave\nGilbert, AZ 85233","type":"business","classy":"false","lot":"true","Caters":"true","validated":"false","trendy":"false","Price Range":"2","romantic":"false","upscale":"false","street":"false","casual":"true","Good for Kids":"true","Outdoor Seating":"false","categories":"Restaurants","state":"AZ","Takes Reservations":"false","longitude":"-111.841114","Waiter Service":"false","garage":"false","intimate":"false","stars":"3.0","dinner":"true","Accepts Credit Cards":"true","latenight":"false","Good For Groups":"true","Noise Level":"average","Take-out":"true","dessert":"false","Wheelchair Accessible":"true","brunch":"false","divey":"false","name":"QQ Grill","Attire":"casual","Delivery":"false","breakfast":"false","valet":"false","touristy":"false","business_id":"7UZQ6qGX8lP7a3jokfYVjQ","open":"false"}
var query= <%=request.getParameter("json")%>;
//var reviews="Mr Hoagie is an institution. Walking in, it does seem like a throwback to 30 years ago, old fashioned menu board, booths out of the 70s, and a large selection of food. Their speciality is the Italian Hoagie, and it is voted the best in the area year after year. I usually order the burger, while the patties are obviously cooked from frozen, all of the other ingredients are very fresh. Overall, its a good alternative to Subway, which is down the road Excellent food. Superb customer service. I miss the mario machines they used to have, but it's still a great place steeped in tradition Yes this place is a little out dated and not opened on the weekend. But other than that the staff is always pleasant and fast to make your order. Which is always spot on fresh veggies on their hoggies and other food. They also have daily specials and ice cream which is really good. I had a banana split they piled the toppings on. They win pennysaver awards ever years i see why. All the food is great here. But the best thing they have is their wings. Their wings are simply fantastic!!  The \"Wet Cajun\" are by the best & most popular.  I also like the seasoned salt wings.  Wing Night is Monday & Wednesday night, $0.75 whole wings!\n\nThe dining area is nice. Very family friendly! The bar is very nice is well.  This place is truly a Yinzer's dream!!  \"Pittsburgh Dad\" would love this place n'at! We checked this place out this past Monday for their wing night. We have heard that their wings are great and decided it was finally time to check it out. Their wings are whole wings and crispy, which is a nice change of pace. I got their wet Cajun sauce and garlic butter wings. The Cajun did not have a bold enough flavor for me and their sauce is too thin. The sauce was also thin for the garlic butter, but that is more expected. They were better than average, but I don't like seeing all the sauce resting at the bottom of the boat. I would definitely come try this place out again to sample some of the other items on the menu, but this will probably not become a regular stop for wings anytime soon. Wing sauce is like water. Pretty much a lot of butter and some hot sauce (franks red hot maybe).  The whole wings are good size and crispy, but for $1 a wing the sauce could be better. The hot and extra hot are about the same flavor/heat.  The fish sandwich is good and is a large portion, sides are decent.";
var reviews = "";
var counts="";

	  
   // var query = document.getElementById("query").value;
		
    $.ajax({
        type: "POST",
        url: "ReviewSearchServlet", //&quot;/AjaxServletCalculator&quot;,
        data: {
            "business_id": query.business_id
        },
        //contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        //contentType: 'text/html; charset=utf-8',
        dataType: "json",
        async : false,
        //if received a response from the server
        success: function(json) {
       	 //console.log(json.wordcloud);
			// displayReviews(json.reviews);
        	calculateWordCount(JSON.stringify(json.wordcloud));
        	calculateBarChart(JSON.stringify(json.VeryNegative),JSON.stringify(json.Negative),JSON.stringify(json.Neutral),JSON.stringify(json.Positive),JSON.stringify(json.VeryPositive));
        	 //console.log(JSON.stringify(json.veryNeg));
        	 //console.log(JSON.stringify(json.neg));
        	 //console.log(JSON.stringify(json.neutral));
        	 //console.log(JSON.stringify(json.positive));
        	 //console.log(JSON.stringify(json.Vpositive));

            //alert(JSON.stringify(json));
            //callBackFunc(json);
        }
    });

   //  function displayReviews(review){
   //  	var a=review.split("\\n");
    	
   //  	// console.log(a.length);
   //  	for(i=0;i<a.length;i++){
    		
   //  		var p=document.createElement("p");
   //      	p.style.fontSize="20px";
   //      	a[i]=a[i].replace('[', '');
   //      	a[i]=a[i].replace(']', '');
   //      	a[i]=a[i].replace('"', '');
   //      	a[i]=a[i].replace('/', '');
   //      	a[i]=a[i].replace('\\', '');
			// if(a[i]==""){}
			// else{
   //      	var text=document.createTextNode("*"+". "+a[i]);
			// }
   //      	p.appendChild(text);
        	
   //      	//console.log("Hello");
   //      	//console.log(text);
   //      	document.getElementById("reviews").appendChild(p);
   //  	}
    	
   //  }
	function calculateBarChart(a,b,c,d,e){
		// var t = a+b+c+d+e;
		var t=1;
		// console.log(t);
		var data = [{name:'Very Negative', frequency:a/t}, {name:'Negative', frequency:b/t}, {name:'Neutral', frequency:c/t},
						{name:'Positive', frequency:d/t},{name:'Very Positive', frequency:e/t}];

		var margin = {top: 20, right: 20, bottom: 30, left: 40},
		width = 960 - margin.left - margin.right,
		height = 500 - margin.top - margin.bottom;

		var formatPercent = d3.format(".0%");

		var x = d3.scale.ordinal()
		.rangeRoundBands([0, width], .1);

		var y = d3.scale.linear()
		.range([height, 0])
		domain([0, d3.max(data, function(d) { return d.frequency; })]);

		var xAxis = d3.svg.axis()
		.scale(x)
		.orient("bottom");

		var yAxis = d3.svg.axis()
		.scale(y)
		.orient("left");
		// .tickFormat(formatPercent);

		var colors = d3.scale.ordinal()
		.domain(['Very Negative','Negative','Neutral','Positive','Very Positive'])
		.range(['#ff0000','#ff9999','#ffff00','#ccffcc','#00ff00'])


		var tip = d3.tip()
		.attr('class', 'd3-tip')
		.offset([-10, 0])
		.html(function(d) {
			return "<strong>Frequency:</strong> <span style='color:red'>" + d.frequency + "</span>";
		})

		var svg = d3.select("#bar").append("svg")
		.attr("width", width + margin.left + margin.right)
		.attr("height", height + margin.top + margin.bottom)
		.append("g")
		.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

		svg.call(tip);

		// d3.tsv("data.tsv", type, function(error, data) {
		x.domain(data.map(function(d) { return d.name; }));
		y.domain([0, d3.max(data, function(d) { return d.frequency; })]);

		svg.append("g")
		.attr("class", "x axis")
		.attr("transform", "translate(0," + height + ")")
		.call(xAxis);

		svg.append("g")
		.attr("class", "y axis")
		.call(yAxis)
		.append("text")
		.attr("transform", "rotate(-90)")
		.attr("y", 6)
		.attr("dy", ".71em")
		.style("text-anchor", "end")
		.text("Frequency");

		svg.selectAll(".bar")
		.data(data)
		.enter().append("rect")
		.attr("class", "bar")
		.attr("fill", function(d, i) { return colors(d.name) })
		.attr("x", function(d) { return x(d.name); })
		.attr("width", x.rangeBand([0,20]))
		.attr("y", function(d) { return y(d.frequency); })
		.attr("height", function(d) { return height - y(d.frequency); })
		.on('mouseover', tip.show)
		.on('mouseout', tip.hide)

		// });

		function type(d) {
			d.frequency = +d.frequency;
			return d;
}
		// var svg = d3.select("svg"),
		// margin = {top: 20, right: 20, bottom: 30, left: 40},
		// width = +svg.attr("width") - margin.left - margin.right,
		// height = +svg.attr("height") - margin.top - margin.bottom;
		// var dataMin = d3.min(d3.values(data));
		// var dataMax = d3.max(d3.values(data)); 
		
		// // prescale = d3.scale.linear()
  // //                  .domain([dataMin, dataMax])
  // //                  .range([0,1]);
        
  // //       yScale = d3.scale.linear()
  // //                .domain([0, 1])
  // //                .range([bottom, top]);

  //       formatter = d3.format(".0%");

		// var x = d3.scale.ordinal().rangeRound([0, width]).padding(0.1),
		// y = d3.scale.linear().rangeRound([height, 0]);

		// var g = svg.append("g")
		// .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

		// // d3.tsv(sentiments, function(d) {
		// // 	d.frequency = +d.frequency;
		// // 	return d;
		// // }, function(error, data) {
		// // 	if (error) throw error;

		// x.domain(data.map(function(d) { return d.name; }));
		// y.domain([0, d3.max(data, function(d) { return d.frequency; })]);

		// g.append("g")
		// .attr("class", "axis axis--x")
		// .attr("transform", "translate(0," + height + ")")
		// .call(d3.axisBottom(x));

		// g.append("g")
		// .attr("class", "axis axis--y")
		// .call(d3.axisLeft(y))//.ticks(10, "%"))
		// // .scale(yScale)
		// .tickFormat(formatter)
		// .append("text")
		// .attr("transform", "rotate(-90)")
		// .attr("y", 6)
		// .attr("dy", "0.71em")
		// .attr("text-anchor", "end")
		// .text("Frequency");

		// g.selectAll(".bar")
		// .data(data)
		// .enter().append("rect")
		// .attr("class", "bar")
		// .attr("x", function(d) { return x(d.name); })
		// .attr("y", function(d) { return y(d.frequency); })
		// .attr("width", x.bandwidth())
		// .attr("height", function(d) { return height - y(d.frequency); });
	}
		// });
	// 	var categories= ['Very Negative', 'Negative', 'Neutral', 'Positive', 'Very Positive'];

	// 	var dollars = [a,b,c,d,e];

	// 	var colors = ['#0000b4','#0082ca','#0094ff','#0d4bcf','#0066AE','#074285','#00187B','#285964','#405F83','#416545','#4D7069','#6E9985','#7EBC89','#0283AF','#79BCBF','#99C19E'];

	// 	var grid = d3.range(25).map(function(i){
	// 		return {'x1':0,'y1':0,'x2':0,'y2':400};
	// 	});

	// 	var tickVals = grid.map(function(d,i){
	// 		if(i>0){ return i*10; }
	// 		else if(i===0){ return "100";}
	// 	});

	// 	var xscale = d3.scale.linear()
	// 					.domain([0,100])
	// 					.range([0,722]);

	// 	var yscale = d3.scale.linear()
	// 					.domain([0,categories.length])
	// 					.range([0,400]);

	// 	var colorScale = d3.scale.quantize()
	// 					.domain([0,categories.length])
	// 					.range(colors);

	// 	var canvas = d3.select('#bar')
	// 					.append('svg')
	// 					.attr({'width':700,'height':550});

	// 	var grids = canvas.append('g')
	// 					  .attr('id','grid')
	// 					  .attr('transform','translate(150,10)')
	// 					  .selectAll('line')
	// 					  .data(grid)
	// 					  .enter()
	// 					  .append('line')
	// 					  .attr({'x1':function(d,i){ return i*30; },
	// 							 'y1':function(d){ return d.y1; },
	// 							 'x2':function(d,i){ return i*30; },
	// 							 'y2':function(d){ return d.y2; },
	// 						})
	// 					  .style({'stroke':'#adadad','stroke-width':'1px'});

	// 	var	xAxis = d3.svg.axis();
	// 		xAxis
	// 			.orient('bottom')
	// 			.scale(xscale)
	// 			.tickValues(tickVals);

	// 	var	yAxis = d3.svg.axis();
	// 		yAxis
	// 			.orient('left')
	// 			.scale(yscale)
	// 			.tickSize(2)
	// 			.tickFormat(function(d,i){ return categories[i]; })
	// 			.tickValues(d3.range(17));

	// 	var y_xis = canvas.append('g')
	// 					  .attr("transform", "translate(150,0)")
	// 					  .attr('id','yaxis')
	// 					  .call(yAxis);

	// 	var x_xis = canvas.append('g')
	// 					  .attr("transform", "translate(150,400)")
	// 					  .attr('id','xaxis')
	// 					  .call(xAxis);

	// 	var chart = canvas.append('g')
	// 						.attr("transform", "translate(150,0)")
	// 						.attr('id','bars')
	// 						.selectAll('rect')
	// 						.data(dollars)
	// 						.enter()
	// 						.append('rect')
	// 						.attr('height',17)
	// 						.attr({'x':0,'y':function(d,i){ return yscale(i)-9; }})
	// 						.style('fill',function(d,i){ return colorScale(i); })
	// 						.attr('width',function(d){ return d*7; });


	// 	var transit = d3.select("svg").selectAll("rect")
	// 					    .data(dollars)
	// 					    .transition()
	// 					    .duration(1000) 
	// 					    .attr("width", function(d) {return xscale(d); });

	// 	var transitext = d3.select('#bars')
	// 						.selectAll('text')
	// 						.data(dollars)
	// 						.enter()
	// 						.append('text')
	// 						.attr({'x':function(d) {return xscale(d)-200; },'y':function(d,i){ return yscale(i)+7; }})
	// 						.text(function(d){ return d; }).style({'fill':'#fff','font-size':'14px'});

	// }

	function calculateWordCount(json){
		var counts=wordcount(json);  
		//var frequency=[];
	    
	    var wordcountarray = [];
		for(var key in counts){
			if (counts.hasOwnProperty(key)) {
				wordcountarray.push({"text" : key, "size" : (counts[key])*7})
	    	  }
			
			
		}
		var frequency_list = wordcountarray;
		// console.log(frequency_list);
		
		var color = d3.scale.linear()
        .domain([0,1,2,3,4,5,6])
        .range(["#ddd", "#ccc", "#bbb", "#aaa", "#999", "#888", "#777", "#666", "#555", "#444", "#333", "#222"]);

		d3.layout.cloud().size([800	, 550])
        .words(frequency_list)
        .rotate(0)
        .fontSize(function(d) { return d.size; })
        .on("end", draw)
        .start();

function draw(words) {
    d3.select("#word").append("svg")
            .attr("width", 700)
            .attr("height", 550)
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
		


    
    

    
 
</script>

<div style="width: 40%;">
    

</div>


	  
	    

</html>