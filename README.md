# MMrestServer
A (very) basic **RESTserver** for [MicroManager](https://micro-manager.org/).

## Getting started
There are two ways to use this:

* Use the precompiled .jar directly
    Download the [/dist/RestServer.jar](https://github.com/MattNeuro/MMrestServer/blob/master/dist/RestServer.jar) and place it in your MicroManager plugins folder. 
    Normally, this would be somewhere like /Micro-Manager-1.4/mmplugins/
* Clone or download the entire project, and open in Netbeans. 
    See the [Writing plugins for Micro-Manager](https://micro-manager.org/wiki/Writing_plugins_for_Micro-Manager) page for detailed instructions on how to proceed from there.

Once the plugin is in place, it needs to be activated after MicroManager is started by going to the Plugins menu option and selecting **RestServer**. 
No dialogue is currently shown, though the console output should list whether the REST server has become active.

## Consuming the REST service
Once the plugin is active, it will bind to the local address on port 8000. 
A short documentation on how to consume it is then available at [http://localhost:8000/](http://localhost:8000/). 


