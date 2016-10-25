from jnius import autoclass

MainClass = autoclass('Main')   #Import the Main.class file as MainClass
instance = MainClass()          #Create an instance of the class
i = instance.call_func();       #Call the method

print "i = " + str(i)
