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
	fileObject = open("answers/code.java", "w")
	fileObject2 = open("answers/temp.txt","w")
	# obtain audio from the microphone
	s = ""
	with sr.Microphone() as source:
		while True:

			text = recognise(source)
			fileObject2.write(text+'\n')
			fileObject2.write(s+'\n')
			
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
					Predict.assignCode(s)
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
				elif text =="\n" or text ==" " or text =="\t" :
					s = s +"" + text
					if text == "\n":
						s = Predict.getWindow(text,s);
					window.clear()
					window.addstr(0, 0,s);
					window.refresh()
				else:
					s = Predict.getWindow(text,s)
					window.clear()
					window.addstr(0, 0,s);
					window.refresh()

	fileObject2.close()

def main():
	curses.wrapper(foo)

main()
