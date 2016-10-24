import speech_recognition as sr
import pyaudio
# obtain audio from the microphone
r = sr.Recognizer()

with sr.Microphone() as source:
	
	# Adjusts parameters for ambient noise
	r.adjust_for_ambient_noise(source, duration = 5)
	
	# Set dynamic_energy_threshold to false 
	# if you do not want the threshold to change automatically
	r.dynamic_energy_threshold = False
	
	# set energy_threshold level
	r.energy_threshold = 800
	
	print("Say something!")
	audio = r.listen(source)

# recognize speech using Sphinx
try:
	print("Sphinx thinks you said " + r.recognize_sphinx(audio))
except sr.UnknownValueError:
	print("Sphinx could not understand audio")
except sr.RequestError as e:
	print("Sphinx error; {0}".format(e))
	
try:
    # for testing purposes, we're just using the default API key
    # to use another API key, use `r.recognize_google(audio, key="GOOGLE_SPEECH_RECOGNITION_API_KEY")`
    # instead of `r.recognize_google(audio)`
	print("Google Speech Recognition thinks you said " + r.recognize_google(audio))
except sr.UnknownValueError:
	print("Google Speech Recognition could not understand audio")
except sr.RequestError as e:
	print("Could not request results from Google Speech Recognition service; {0}".format(e))
	


