package com.Search.SearchEngine;

import java.util.ArrayList;

public class WhatToThink {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String topic = "ICCT20WC";
		 String tweets[]={"A guy finds a pickled wasp in his salad. He calls the waiter to his table. There is a pickled wasp in my salad! How did a pickled wasp get in my salad?!? The waiter looks at the dead bug and goes  ask the chef how it got there but I'll have to blame the pickling on our bartender"};
		 int neg=0;
		 int pos=0;
		 NLP.init();
	        for(String tweet : tweets) {
	            System.out.println(tweet + " : " + NLP.findSentiment(tweet));
	            if(NLP.findSentiment(tweet)==1){
	            	neg=neg+1;
	            }
	            if(NLP.findSentiment(tweet)>1){
	            	pos=pos+1;
	            }
	            
	            
	        }
	}

}
