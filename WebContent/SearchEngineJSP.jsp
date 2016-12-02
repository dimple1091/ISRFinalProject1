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
      <script src="js/jquery-1.11.1.min.js"></script>
      <!-- lightbox -->
      <script src="js/templatemo_tab.js"></script>
      <!-- lightbox -->
      <script src="js/bootstrap-collapse.js"></script>
      <script src="js/jquery-ui.min.js"></script>
      <!-- popular tab -->
      <script src="js/json2.js"></script>
   </head>
   <body>
      <form class="form-wrapper cf" style="margin-left:30px" method="post" action="SearchEngine">
         <input style="height:39px;background:white;color:black" type="text" placeholder="Search here..." required="" id="query" /> 
         <button type="button" id="buttonId" onclick="callServlet()">Search</button>
      </form>
      <div class="col-md-8 margin30">
         <div class="templatemo_homemid_right shadow">
            <div class="box">
               <div class="icon">
                  <img style="margin-top:0" src="images/logo.jpg" alt="templatemo home icon 1" />
               </div>
               <div class="text">
                  <div class="title" style="font-family:serif;color:#4C4CFF">
                     <b>1. Arsenal Cider House &amp; Wine Cellar</b>
                  </div>
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
         function callServlet() {
		   alert("");
		   var query=document.getElementById("query").value;
		   alert(query);
			$.ajax({
				type: "POST",
				url: "SearchEngine",  //&quot;/AjaxServletCalculator&quot;,
				data: {
					"query":query
				},
				dataType: "json",
				//if received a response from the server
				success: function (json) {
					console.log(JSON.stringify(json));
					alert(JSON.stringify(json));
				}
			});
         }
         
         /*$(function () {
                 
                      $(&#39;#buttonId&#39;).click(function() {
                              alert(&quot;button&quot;);
                      callServlet();
                  });  
         });*/
         
      </script>
   </body>
</html>