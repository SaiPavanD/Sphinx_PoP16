import speech_recognition as sr
import curses
import time
from jnius import autoclass

Predict = autoclass("Predict");
# instance = MainClass();
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
	fileObject = open("answers/code.minijava", "w")
	# obtain audio from the microphone
	s = ""
	with sr.Microphone() as source:
		while True:

			text = recognise(source)
			if text != "null" :
				if text	== "exit":
					fileObject.write(s)
					fileObject.close()		    			    		    	
					break
				elif "back" in text:
					words = text.split(" ")
					for j in range(0,len(words)):
						if words[j] == "back":
							i = s.rfind(" ")
							s = s[:i]
					window.clear()
					window.addstr(0, 0,s);
					window.refresh()
				elif "up" in text:
					words = text.split(" ")
					for j in range(0,len(words)):
						if words[j] == "up":
							i = s.rfind("\n")
							s=s[:i]
					window.clear()
					window.addstr(0, 0,s);
					window.refresh()
				elif text == "tab":
					s = s+"\t"
					window.clear()
					window.addstr(0, 0,s);
					window.refresh()
				elif text == "":
					s = s +"" + text
					window.clear()
					window.addstr(0, 0,s);
					window.refresh()
				elif text =="\n":
					s = s +"" + text
					window.clear()
					window.addstr(0, 0,s);
					window.refresh()
				else:
					s = Predict.getWindow(text)
					window.clear()
					window.addstr(0, 0,s);
					window.refresh()

def main():
	curses.wrapper(foo)

main()
