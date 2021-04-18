# Genome Size

## ABSTRACT

**C**onserved **R**atio **C**alculator is designed to calculate the conserved sites ratio quickly using Bam & Bed in hexaploid wheat, which may be expanded to other species in the future.



## PREREQUISITES

**Java:**  Runtime >= 52.0 (Version >= 8)

**Samtools:**



## INSTALLATION

Download the latest version `CRC.jar`  in Release.



## Usage

Use command line `java -jar CRC.jar ...`



## PARAMETERS

The following parameters are must.

- **-b:** Bed file location
- **-p:** Bam file location, you could provide both directory and file name to this parameter. CRC will identify all the files end with `.bam` if you provide a path.
- **-r:** Bp per read. This parameter may disappear in next version for it can be obtained from the bam directly.



## INPUT

The input files are provided to CRC using the `-p -b` parameters, in which the `-p` specify the path included bam files and `-b` specify the bed file.

They are not required to locate in the same directory as your current.



## OUTPUT

Two out files named `ABDSplit.out` & `debug.log` will be created in your current directory if you have not specify the parameter `-o` .

There are file name, conserved sites in A, B, D, all conserved sites, reads aligned to A, B, D, all reads, conserved ratio in A, B, D and general conserved ratio respectively from the front to back in `ABDSplit.out`.

And you could submit the `debug.log` to the issue if you need help.



## PARAMETERS OPTIONAL

- **-o:** Out file location, optional
- **-t:** Thread, default to 32, optional
- **-sf:** The index of start file, optional
- **-ef:** The index of end file, optional

These two parameters are designed to choose the bam files if you pass a path to `-p`. Ex, your directory have 100 bam files named form 001 - 100, `-sf 1 -ef 50` can be used if only 001 - 050 files need to be calculated.



## EXAMPLE

```shell
java -jar -Xmx100g -Xms10g CRC.jar -p ./ -b ./example.bed -r 100
```
