import speech_recognition as sr
import curses
import time
from jnius import autoclass

MainClass = autoclass("Predict");
instance = MainClass();
r = sr.Recognizer()

def recognise(source):
	audio = r.listen(source)

	try:
	    return r.recognize_google(audio)
	except sr.UnknownValueError:
	    return "null"
	except sr.RequestError as e:
	    return "null"

def foo(window):

	# obtain audio from the microphone
	s = ""
	with sr.Microphone() as source:
		while True:

		    text = recognise(source)
		    if text	== "exit":
		    	break
		    elif text == "next":
		    	s = s + "\n"
		    	window.clear()
		    	window.addstr(0, 0,s);
		    	window.refresh()

		    elif "back" in text:
		    	words = text.split(" ")
		    	for j in range(0,len(words)):
		    		if words[j] == "back":
				    	i = s.rfind(" ")
				    	s = s[:i]
		    	window.clear()
		    	window.addstr(0, 0,s);
		    	window.refresh()

		    else:
		    	s = instance.getWindow(text,s)
		    	window.clear()
		    	window.addstr(0, 0,s);
		    	window.refresh()

def main():
	curses.wrapper(foo)

main()
