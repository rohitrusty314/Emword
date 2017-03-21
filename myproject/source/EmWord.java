import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.Color;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.Document;



class ColorPane extends JTextPane{
// to change the color(i.e. flag) the incorrect word only.
     public void append(Color c, String s,int startPos) { 
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, c);

            int len = getDocument().getLength(); // same value as getText().length();  
            try{                   
                setCaretPosition(len);              // place caret at the end (with no selection)
	            setCharacterAttributes(aset, false);
                replaceSelection(s); // there is no selection, so inserts at caret
	// for further input by the user reset the foreground color to black
	            len = getDocument().getLength();
	            setCaretPosition(len);
	            AttributeSet aset1 = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground,Color.black);
	            setCharacterAttributes(aset1, false);
            } catch(Exception e){
                  System.out.println("exception occured in append:" + e);
            }
        }
   // to replace the incorrect word in red by the selected correct word in black
     public void replace(Color c, String s,int startPos) { 
	        StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, c);

            int len = startPos;                    // same value as getText().length();
            try{                  
                setCaretPosition(len); // place caret at the end (with no selection)
	            setCharacterAttributes(aset, false);
                replaceSelection(s); // there is no selection, so inserts at caret
				
	// for further input by the user reset the foreground color to black
	            len = getDocument().getLength();
	            setCaretPosition(len);
	            AttributeSet aset1 = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground,Color.black);
	            setCharacterAttributes(aset1, false);
            } catch(Exception e){
                  System.out.println("exception caught in replace : " + e);
            }
        }
    }



/* Read the known list of correct words from a file and store it into a hash table */
class WordList{
      Hashtable list; 
      public WordList(){                                  
	     list = new Hashtable(300);                     // creates a hashtable with initial capacity 300
         try{
		     String s,token;
			 BufferedReader dict = new BufferedReader(new FileReader("a.txt"));     // opens the file to read the words
			 while((s = dict.readLine()) != null ) {                                // read until the end of the file
			     StringTokenizer st = new StringTokenizer(s);                       // constructs a string tokenizer for the specified string   
				 while(st.hasMoreTokens()){                                         //  Tests if there are more tokens available from tokenizer's string
				     token = st.nextToken();                                        // Returns the next token from this string tokenizer.
					 list.put(token, token);                                        //  Maps the specified key to the specified value in this hashtable.
					}
				}
			 dict.close();
			} catch(Exception e){
			      System.out.println("Exception occured: " + e);
			}
		}
    }		

	class Ele{                     //element that is to be ibserted in a stack
	 String sr;
	 int index;
	 }

public class EmWord extends WordList{
     Stack<Ele> wrongList = new Stack<Ele>();               //instance of Stack class
	 JFrame helpFrame;                                      // GUI componenets
     JButton btnReplace;
	 JButton btnIgnoreOne;
	 JButton btnIgnoreAll;
	 JButton btnAdd;
	 JButton btnHelp;
	 JButton btnExit;
	 ColorPane txtInput = new ColorPane();                   //ColorPane instance for user input
	 JTextField txtWord; 
	 JList lstOptions;                                        
	 String[] probableWords = {" "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "," "};
	 String[] inWord;
	 int flag = 0;
	 JFrame overLimit;

     public static void main(String[] args){ 
         WordList correctList = new WordList();        //creates a list of correct words
		 EmWord a = new EmWord();
		 try{
		      a.go();
			} catch(Exception exec){
			   System.out.println("Exception occured in main : " + exec);
			}
	    }
	// implements User interface
	  void go(){
          JFrame frame = new JFrame("Em-Word");
          JPanel panel = new JPanel();
          JPanel panel2 = new JPanel();
          JPanel panel3 = new JPanel();
          panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS)); 
	      
		  //button instances
		  btnReplace = new JButton("Replace");
	      btnIgnoreOne = new JButton("IgnoreOne");
	      btnIgnoreAll = new JButton("IgnoreAll");
	      btnAdd = new JButton("Add");
	      btnHelp = new JButton("Help");
	      btnExit = new JButton("Exit");
		  
		  //JList instance to display the probable words generated
          lstOptions = new JList(probableWords);
	      lstOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);         //only single selection
          txtInput.setSize(600,400);
	      
	      JLabel lbl22 = new JLabel("Enter the text to be verified here :");
	      panel2.add(lbl22);
	
	      panel2.add(txtInput);
	      JLabel label = new JLabel("Options for replacement of the wrong Word:");
	     
	      panel2.add(label);
	
	      txtWord = new JTextField();
	     
		 JScrollPane opScroller = new JScrollPane(lstOptions);
	     opScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	     opScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	     panel2.add(txtWord);
	     opScroller.setSize(100,100);
	     panel2.add(opScroller);
	     frame.getContentPane().add(BorderLayout.CENTER, panel2);
		 
	     panel.add(btnReplace);
	     panel.add(btnIgnoreOne);
	     panel.add(btnIgnoreAll);
	     panel.add(btnAdd);
	     panel.add(btnHelp);
	     panel.add(btnExit);
	     panel.setBackground(Color.darkGray);
	     
		 JLabel lbl3 = new JLabel("         Em-Word               " );
	     panel3.add(lbl3);
	     frame.getContentPane().add(BorderLayout.EAST, panel3);
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.getContentPane().add(BorderLayout.SOUTH, panel);
	     frame.setSize(650,450);
	     frame.setVisible(true);
	     
		 
// add listeners for the events
         btnReplace.addActionListener(new ButtonReplaceListener());
	     btnIgnoreOne.addActionListener(new ButtonIgnoreOneListener());
	     btnIgnoreAll.addActionListener(new ButtonIgnoreAllListener());
	     btnAdd.addActionListener(new ButtonAddListener());
	     btnHelp.addActionListener(new ButtonHelpListener());
	     btnExit.addActionListener(new ButtonExitListener());
// listener to a selection in the JList
	     lstOptions.addListSelectionListener(new MyListSelectionListener());
// listen to an Event on pressing space in the txtInput
         txtInput.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("pressed SPACE"), insertSpaceAction.getValue(Action.NAME));
	     txtInput.getActionMap().put(insertSpaceAction.getValue(Action.NAME), insertSpaceAction);
	



        }

// action to be performed on pressing space in the txtInput	
	 public Action insertSpaceAction = new AbstractAction("Insert Space") {
          public void actionPerformed(ActionEvent evt) {
              try {
			      operations();
                } catch (Exception e) {
              
			    }
            }
        };
		
//to verify the words, if in correct flag them and push onto to the stack 
     void operations(){
	     try{
	         String lookup;
	         Ele foo = new Ele();
			 int startPos = 0,i,l;
             String inText = txtInput.getText();          // get all the user input	      
		     inWord = inText.split("\\s");                // split and store into a a string array inWord
			 if(inWord.length>145){
			     overLimit = new JFrame(" word limit Exceeded!!");
				 overLimit.setSize(250,150);
                  overLimit.setVisible(true);
                  JLabel lbl = new JLabel("You have exceeded the Word LIMIT!!!");
                  JPanel panelLimit = new JPanel();
			      panelLimit.setLayout(new BoxLayout(panelLimit, BoxLayout.Y_AXIS));
			      overLimit.getContentPane().add(BorderLayout.CENTER, panelLimit);
			      panelLimit.add(lbl);
                  overLimit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                  JButton btn = new JButton("ok");
                  JPanel panelBtn = new JPanel(); 	
                  panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.Y_AXIS));
			      overLimit.getContentPane().add(BorderLayout.SOUTH, panelBtn);
				  panelBtn.add(btn);
				  btn.addActionListener(new Button2Listener());
				  if(inWord.length>150)
				     System.exit(0);
                }
				
			 for(l = inWord.length-1; l < inWord.length; l++){     
			     lookup = searchForWord(inWord[l]);                //search word, 

				 if(lookup == null){                               // null if not found
			         foo.sr = inWord[l];                           // set the element string value
                     foo.index = l;					               //set the words index value 
					 wrongList.push(foo);                          // push the word on the stack of incorrect words
			    	}
			    }
				
     // to flag the identified in correct word
				for(int j =0 ; j<inWord.length-1;j++){                 // get position of the word to be flagged
				    startPos = startPos +inWord[j].length()+1;				
				}
			
				for(i = inWord.length-1;i < inWord.length; i++){
				  lookup = searchForWord(inWord[i]);
 				  if(lookup == null){
					 int len = startPos+inWord[i].length();             // start position+length of the word
				     txtInput.getDocument().remove(startPos,inWord[i].length());   //remove the word from the specified offset
					 String sp = inWord[i] + " ";                                  // append a space to the word to be inserted in red
					 txtInput.append(Color.red,sp,startPos);                       // append the incorrect word in red foreground
					}
				  else{
					 startPos = startPos + inWord[i].length() + 1;                  // if the word is present update start position
                    }	
                  getProbableWords();                                               // generate probable words for the incorrect word at the top
                }				   
				    
			}catch(Exception e){
			     System.out.println("Exception caught : " + e);
			 }
			 
        }
		
		class Button2Listener implements ActionListener{
	          public void actionPerformed(ActionEvent event){
		         overLimit.setVisible(false);
	            }
	        }
		
	    String searchForWord(String cWord){
		     String uninflectedWord,lookup;
		     // if it is a common mispelling, output the corrected word
             if ((lookup = (String)list.get(cWord)) != null)
             return cWord;
 
             // Remove inflections at end of word and try again ("es", "s", "ing", "ed")
             int length = cWord.length();
 
             // first check for final 's'.
             if (length > 1 && cWord.substring(length - 1).equals("s")) {
                  uninflectedWord = cWord.substring(0, length-1);
 
                 if ((lookup = (String)list.get(uninflectedWord)) != null)
                 return cWord;
               
                }
                // 'es' check
              if (length > 2 && cWord.substring(length-2).equals("es")) {
                   uninflectedWord = cWord.substring(0, length-2);
 
                   if ((lookup = (String)list.get(uninflectedWord)) != null)
                       return cWord;
                   //else not found
                    
                }
				// "ies" in words ending with a "y"
				if (length > 3 && cWord.substring(length - 3).equals("ies")) {
                       uninflectedWord = cWord.substring(0, length-3);
					   uninflectedWord = uninflectedWord + "y";
 
                       if ((lookup = (String)list.get(uninflectedWord)) != null)
                            return cWord;
                }
 
                // for words ending with "ing"
                if (length > 3 && cWord.substring(length - 3).equals("ing")) {
                       uninflectedWord = cWord.substring(0, length-3);
 
                       if ((lookup = (String)list.get(uninflectedWord)) != null)
                            return cWord;
                }
				
                // "ing" in words, ending with a "y"				
			    if (length > 3 && cWord.substring(length - 3).equals("ing")) {
                       uninflectedWord = cWord.substring(0, length-3);
                       uninflectedWord = uninflectedWord + "e";
                       if ((lookup = (String)list.get(uninflectedWord)) != null)
                            return cWord;
                }
				
				// 'ed' check
                
                if (length > 2 && cWord.substring(length - 2).equals("ed")) {
                      uninflectedWord = cWord.substring(0, length-2);
 
                      if ((lookup = (String)list.get(uninflectedWord)) != null)
                          return cWord;
                      
                }
				
				// 'd' check
				if (length > 2 && cWord.substring(length - 1).equals("d")) {
                      uninflectedWord = cWord.substring(0, length-1);
 
                      if ((lookup = (String)list.get(uninflectedWord)) != null)
                          return cWord;
                      
                }
				
				if (length > 2 && cWord.substring(length - 2).equals("al")) {
                      uninflectedWord = cWord.substring(0, length-2);
 
                      if ((lookup = (String)list.get(uninflectedWord)) != null)
                          return cWord;
                      
                }
				
				if (length > 3 && cWord.substring(length - 3).equals("ied")) {
                       uninflectedWord = cWord.substring(0, length-3);
					   uninflectedWord = uninflectedWord + "y";
 
                       if ((lookup = (String)list.get(uninflectedWord)) != null)
                            return cWord;
                }
				
				// for words having .,!? at the end
				if (length > 1 && ((cWord.substring(length - 1).equals(".")) ||(cWord.substring(length - 1).equals(",")) || (cWord.substring(length - 1).equals("!")) ||(cWord.substring(length - 1).equals("?")) )){
                  uninflectedWord = cWord.substring(0, length-1);
 
                 if ((lookup = (String)list.get(uninflectedWord)) != null)
                 return cWord;
				}
 
                // word was not found, even after "uninflecting".
                    return null;
        }
		
		
	// generate probable words for the incorrect word.
		void getProbableWords(){		
		      Enumeration<String> temp;              // instance to store a series os  strings from the hash table
		      for(int i=0;i<30;i++)
			      probableWords[i] = " ";
	          
				String str,str1,str2,str3;
				int indx;
				Ele foo = new Ele();
				int j=0;
				foo = wrongList.peek();            //just retrieve the word at the top of the stack, without popping it
				str = foo.sr;
				indx = foo.index;
		// if the word length is more than 3
				if((str.length()) >3){
				     str1 = str.substring(0,3);
				     temp = list.keys();
				     while((temp.hasMoreElements()) && (j < 30)){            // while there are more elements and words generated is less than 30
				          str2 = temp.nextElement();
					      if(str2.length() > 3)
					          str3 = str2.substring(0,3);
					      else
					         str3 = str2;
				          if(str1.equalsIgnoreCase(str3)){
					          probableWords[j] = str2;
						      j++;
						    }
                        }
				    }
		// for words of length equal to 3, matching upto 2 letters	
				if((str.length()) == 3){
			         str1 = str.substring(0,2);
				     temp = list.keys();
				     while((temp.hasMoreElements()) && (j < 30)){
				         str2 = temp.nextElement();
					     if(str2.length() == 3)
					         str3 = str2.substring(0,2);
					      else
					         str3 = str2;
				     
					      if(str1.equalsIgnoreCase(str3)){
					         probableWords[j] = str2;
						     j++;
						    }
                        }
				    }
			// for words of length equal to 3, matching upto 2 letters of the words greater than 2 
				if((str.length()) == 3){
				     str1 = str.substring(0,2);
				     temp = list.keys();
				     while((temp.hasMoreElements()) && (j < 30)){
				          str2 = temp.nextElement();
					      if(str2.length() > 2)
					         str3 = str2.substring(0,2);
					      else
					         str3 = str2;
				          if(str1.equalsIgnoreCase(str3)){
					          probableWords[j] = str2;
						      j++;
						    }
                        }
				    }
				 
		  // for words of length equal to 2, matching upto 1 letters
				if((str.length()) == 2){
				  	 str1 = str.substring(0,1);
				     temp = list.keys();
				     while((temp.hasMoreElements()) && (j < 30)){
				         str2 = temp.nextElement();
					     if(str2.length() == 2)
					         str3 = str2.substring(0,1);
					     else
					         str3 = str2;
				         if(str1.equalsIgnoreCase(str3)){
					         probableWords[j] = str2;
						     j++;
						    }
                        }
				    }
				 
				// for words of length equal to 2, matching upto 1 letter of the words greater than 2 
				if((str.length()) == 2){
			         str1 = str.substring(0,1);
				     temp = list.keys();
				     while((temp.hasMoreElements()) && (j < 30)){
				         str2 = temp.nextElement();
					     if(str2.length() > 1)
					         str3 = str2.substring(0,1);
					     else
					         str3 = str2;
				         if(str1.equalsIgnoreCase(str3)){
					         probableWords[j] = str2;
						     j++;
						    }
                        }
				    }
				 
				 //for incorrect words of length = 1
			    if((str.length()) == 1){
				     str1 = str.substring(0,1);
				     temp = list.keys();
				     while((temp.hasMoreElements()) && (j < 30)){
				         str2 = temp.nextElement();
					     if(str2.length() > 1)
					         str3 = str2.substring(0,1);
					     else
					         str3 = str2;
				         if(str1.equalsIgnoreCase(str3)){
					         probableWords[j] = str2;
						     j++;
						    }
                        }
				    }
                // if so far less than 30 words have been generated,include words matching one letter
				if(j<29){
				      str1 = str.substring(0,1);
				      temp = list.keys();
				      while((temp.hasMoreElements()) && (j < 30)){
				         str2 = temp.nextElement();
					     if(str2.length() >= 1)
					         str3 = str2.substring(0,1);
					     else
					         str3 = str2;
				         if(str1.equalsIgnoreCase(str3)){
					         probableWords[j] = str2;
						     j++;
						    }
                        }	
			        }
					return;
		}

	// action performed when a selection is made in the listBox	
	    class MyListSelectionListener implements ListSelectionListener{
		  public void valueChanged(ListSelectionEvent le){
		        int index = lstOptions.getSelectedIndex();
	            if(index != -1){
			     txtWord.setText(probableWords[index]);
		    	}
		        else
			     txtWord.setText(" ");
            }
	    }
		
// action when replace button is hit
		
	 class ButtonReplaceListener implements ActionListener{
	      public void actionPerformed(ActionEvent event){
		      String inText = txtInput.getText();             //read input text	      
		      inWord = inText.split("\\s");                    // split into a string array
		      int startPos=0,k,len1 = 0 ;
		      String s,rp;
		      Ele foo = new Ele();
		      int indx;
	          try{
		          if(flag==0){
	                  foo = wrongList.pop();                     // pop the word at the top of the stack
			          s = foo.sr;
			          indx = foo.index;
			          startPos = 0;
			          int m = inWord.length;
			          for(k = inWord.length-1;k >= 0; k--){                  // start searching for the word to be replaced from the end of the input
			             if((s.equals(inWord[k])) && (indx == k)){            // if found
				             startPos = startPos + inWord[k].length()+1;       // update start position and
					         break; 											//exit the loop
					        }
				         else
				             startPos = startPos + inWord[k].length()+1 ;
			            }
			            int length = inWord[k].length();
				        for( int l = 0; l<inWord.length;l++)                  // get total length of the user input
				           len1 = len1 + inWord[l].length() + 1;
				        startPos =  len1 - startPos;                           // start position from the beginning of the input
			            String repWord = txtWord.getText();
			            int len = startPos+repWord.length();
				  
				// if symbols like .,!? were at the end of the incorrect word
			            if (length > 1 && (inWord[k].substring(length - 1).equals(".")))
                          rp = repWord + ". " ;
			            else if (length > 1 && (inWord[k].substring(length - 1).equals(",")))
						 rp = repWord + ", " ;
			            else if (length > 1 && (inWord[k].substring(length - 1).equals("!")))
                         rp = repWord + "! " ;
			            else if (length > 1 && (inWord[k].substring(length - 1).equals("?")))
                         rp = repWord + "? " ;
	    	            else
			             rp = repWord + " ";
			            txtInput.getDocument().remove(startPos,inWord[k].length());
			            txtInput.replace(Color.black,rp,startPos);
			            txtWord.setText("");
			            getProbableWords();
		            }
		            else{                                        // if ignore operation has been performed before replace operation
		               foo = wrongList.pop();
			           s = foo.sr;
		     		   indx = foo.index;
			           startPos = 0;
			           int m = inWord.length;
			           for(k = inWord.length-1;k >= 0; k--){
			              if((s.equals(inWord[k])) && (indx == k)){
				             System.out.println("a wrong word");
				             startPos = startPos + inWord[k].length()+1;
					         break;
					        }
				            else
				              startPos = startPos + inWord[k].length()+1 ;
			            }
			            int length = inWord[k].length();
				        for( int l = 0; l<inWord.length;l++)
				          len1 = len1 + inWord[l].length() + 1;
	         	      startPos =  len1 - startPos;
			          String repWord = txtWord.getText();
		              int len = startPos+repWord.length();
				      if (length > 1 && (inWord[k].substring(length - 1).equals(".")))
                              rp = repWord + ". " ;
		              else if (length > 1 && (inWord[k].substring(length - 1).equals(",")))
							  rp = repWord + ", " ;
			          else if (length > 1 && (inWord[k].substring(length - 1).equals("!")))
                             rp = repWord + "! " ;
			          else if (length > 1 && (inWord[k].substring(length - 1).equals("?")))
                             rp = repWord + "? " ;
	    	          else
			                 rp = repWord +" ";
			          txtInput.getDocument().remove(startPos,inWord[k].length());
			          txtInput.replace(Color.black,rp,startPos);
			 
			          txtWord.setText("");
					  getProbableWords();		  
					  return;
		            }
            
			    }catch(EmptyStackException e){
                 System.out.println("Stack Empty");
                }catch(Exception ex){
			     System.out.println("replace bad location");
			    }
            }
        }	

    // ignore one instance of the latest incorrect word		
	 class ButtonIgnoreOneListener implements ActionListener{
	     public void actionPerformed(ActionEvent event){
		     String inText = txtInput.getText();	      
		     inWord = inText.split("\\s");
		     int startPos=0,k,len1 = 0 ;
		     Ele foo = new Ele();
		     int indx;
		     String s;
	         try{
	             foo = wrongList.pop();     //pop the word to be replaced
			     s = foo.sr;
			     indx = foo.index;
			     startPos = 0;
			     int m = inWord.length;
			     for(k = inWord.length-1;k >= 0; k--){                       //locate the word popped to be ignored in the input text
			         if((s.equals(inWord[k])) && (k == indx)){
				         startPos = startPos + inWord[k].length()+1;
					     break;
				    	}
				     else
				         startPos = startPos + inWord[k].length()+1 ;
			        }
				 for( int l = 0; l<inWord.length;l++)
				      len1 = len1 + inWord[l].length() + 1;
	    		 startPos =  len1 - startPos;
				 String repWord = inWord[k];
			     int len = startPos+repWord.length();
			     String rp = repWord +" ";
			     txtInput.getDocument().remove(startPos,inWord[k].length());    // remove the incorrect word in red
			     txtInput.replace(Color.black,rp,startPos);                      // replace with the same word in black
			     txtWord.setText("");                         
			     getProbableWords();
			     return;
                }catch(EmptyStackException e){
                System.out.println("Stack Empty");
                }
				catch(Exception ex){
				System.out.println("replace bad location");
				}
	        }
        }
		
	 // action performed to ignore all the instances of the latest in correct word
	 class ButtonIgnoreAllListener implements ActionListener{
	        public void actionPerformed(ActionEvent event){
			 
		      Stack<Ele> temp = new Stack<Ele>();
		      Ele foo = new Ele();
		      Ele tempFoo = new Ele();
		      Ele listFoo = new Ele();
		      int indx;
		      String s;
	          try{
                 flag = 1;
       		     foo = wrongList.peek();       // pop 
			     s = foo.sr;
			     indx = foo.index;
			 
			// remove the multiple entries of  the word to be ignored
			     while(!(wrongList.empty())){
			         tempFoo=wrongList.pop();
				     if(!(s.equals(tempFoo.sr))){               //if a different entry
					      temp.push(tempFoo);						  //push into a temporary stack
			
						}
						else{
						   wrongList.push(tempFoo);
						   btnIgnoreOne.doClick();
						   }
				    }
				 while(!(temp.empty())){                       // restore the different entries from the temporary stack
				     listFoo = temp.pop();
				     wrongList.push(listFoo);
				    }
			
				    if(!(wrongList.empty()))
				      getProbableWords();
                    return;					  
	            } catch(EmptyStackException e){
                     System.out.println("Stack Empty");
                } catch(Exception ex){
				  System.out.println("replace bad location");
				}
				
		    }
	    }

    // to add a word into the file that is not present in the advance
	
     class ButtonAddListener implements ActionListener{
	      public void actionPerformed(ActionEvent event){	
	          Ele temp = new Ele();
			  temp = wrongList.peek();     // retrieve the word at the top, without popping
		      list.put(temp.sr,temp.sr);    // also put it into the list of words used for verification
		      String lookup;
		      btnIgnoreAll.doClick();         // explicitly invoke action for button ignore all
		      try{
		          FileWriter out = new FileWriter("a.txt",true);
                  BufferedWriter writer = new BufferedWriter(out);
                  writer.newLine();
                  writer.write(temp.sr);
                  writer.close();
		        } catch(IOException e){
		          System.out.println("Exception" + e);
			    }
            }
	    }

// to invoke action to be performed when hellp button is clicked
		
     class ButtonHelpListener implements ActionListener{
         public void actionPerformed(ActionEvent event){
	         helpFrame = new JFrame("Help");     // get a another frame
			   JLabel lblIn = new JLabel("Usage:");
			   JLabel lblInfo = new JLabel("1.enter the text to be verified.\n");
			   JLabel lblInfo2 = new JLabel("2.each Word is to be separated by one white space\n");
			   JLabel lblInfo4 = new JLabel("3.to get options for the incorrect word scroll the list box\n");
			   JLabel lblInfo3 = new JLabel("3.Avoid using back-spaces\n");
			   JButton btnExit2 = new JButton("OK!!");
			   JPanel panelHelp = new JPanel();
			   panelHelp.setLayout(new BoxLayout(panelHelp, BoxLayout.Y_AXIS));
			   helpFrame.getContentPane().add(BorderLayout.CENTER, panelHelp);
			   panelHelp.add(lblIn);
			   panelHelp.add(lblInfo);
			   panelHelp.add(lblInfo2);
			   panelHelp.add(lblInfo3);
			   panelHelp.add(btnExit2);
			   helpFrame.setSize(450,350);
               helpFrame.setVisible(true);
			   btnExit2.addActionListener(new ButtonExit2Listener());
		    }
			
		 class ButtonExit2Listener implements ActionListener{
	          public void actionPerformed(ActionEvent event){
		         helpFrame.setVisible(false);
	            }
	        }
		
	    }
	 
	   class ButtonExitListener implements ActionListener{
	        public void actionPerformed(ActionEvent event){
		       System.exit(0);				
	        }
	    }

	}            // end of class EmWord

	