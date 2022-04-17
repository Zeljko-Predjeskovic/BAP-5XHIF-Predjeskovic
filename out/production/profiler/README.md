# NotSoQuickSort Profiling

Profiling mit IntelliJ auf Windows 10.


## Konfiguration


### Cpu - nur cpu
 ![configure profiler1](images/profilesettings1.PNG)

### Wall-Clock - alle threads
 ![configure profiler2](images/profilesettings2.PNG)

### Allocation - nur memory
 ![configure profiler3](images/profilesettings3.PNG)
 


## 1. Wall-Clock

![wall](images/wall.PNG)

## 2. Allocation

AllocTracer is not supported for Windows. WSL stattdessen:

![allo](images/alloc.PNG)

## 3. Cpu

![cpu](images/cpu.PNG)

## Bemerkungen

Je größer die Liste, umso mehr stack traces gibt es.
Es werden mehr sort() und next() methoden aufgerufen.



## Ergebnis


