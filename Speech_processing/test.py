from jnius import autoclass

Predict = autoclass("Predict");
print Predict.getWindow("class ab")
print Predict.getWindow("open public static")
print Predict.getWindow("vod main open spring open close arg close")
