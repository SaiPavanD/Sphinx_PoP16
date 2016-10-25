from jnius import autoclass

MainClass = autoclass('Main')
instance = MainClass()
i = instance.call_func();

print "i = " + str(i)
