#!/bin/bash

debug=0
minimize=0

#	check number of arguments. Should be two
if [ "$#" -ne 3 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

arg1=$1
arg2=$2

#	arg3 is either union or product encoded as -u and -p

if [[ ( "$3" != "-u" ) &&  ( "$3" != "-p" ) && ( "$3" != "-x" ) ]]; then
	 echo "Unsupported strategy $3"
	 exit 1
fi

if [ "$3" = "-u" ]; then
		strategy=0
		stratname="union"
    echo "Computing refinements and UNION"
fi

if [ "$3" = "-p" ]; then
		strategy=1
		stratname="product"
    echo "Computing refinements with PRODUCTS"
fi


if [ "$3" = "-x" ]; then
		strategy=2
		stratname="exact"
    echo "Computing refinements with EXACT cache"
fi

# ??
concrete="concrete"

#
# observerfile="observer-all.xml"

#
# add ../bin to our paths to get access to our programs
export PATH=`pwd`/../bin/:$PATH

# basename is the name without extension toto/titi/tata.xml has toto/titi/tata as basename
filebasename=`basename $arg1 .xml`

#	directory with computations has name $filebasename-$stratname
tgtdir=$filebasename-$stratname
echo "Saving resu.initial in $tgtdir"

# sanity clearing of existing computations
echo "Clearing/deleting directory" $tgtdir

# create a directory
rm -Rf $tgtdir
mkdir -p $tgtdir


# copy file to be processed in uppaal_file.xml
# union
if [ $strategy	-eq 0 ]; then
# if we choose UNION, we have to change the chan option for Icache[] and add an initial observer
	# systemline=$(cat -n $arg1 | grep "<system>" | awk '{print $1}')
	# filelines=$(wc -l $arg1 | awk '{print $1}')
	# tailline=`expr $filelines - $systemline`
	# head -n `expr $systemline - 1`  $arg1 >tmp.xml
	# cat $observerfile >>tmp.xml
	# tail -n `expr $tailline + 1` $arg1 >>tmp.xml

	# now add observer to system line
	# remove all the lines with system theProg etc
	cat $arg1 | sed -e 's/\(system.*;\)/\/\/\1/g' >tmp.xml
	# add the line with theProg and observer
	cat tmp.xml | sed -e 's/\(<\/system>\).*/system theProg, FetchStage, InstructionCache, observer;\1/' >$tgtdir/uppaal_file.xml
fi
# product
if [ $strategy	-eq 1 ]; then
	# add broadcast to chan Icache[2]
	cat $arg1 | sed  -e 's/\(chan.*Icache.*;\)/broadcast \1/g' -e 's/\(system.*;\)/\/\/\1/g' -e 's/\(<\/system>\).*/system theProg, FetchStage, InstructionCache;\1/' -e 's/<\/system>/\'$'\n<\/system>/g' >$tgtdir/uppaal_file.xml
fi
# exact
if [ $strategy	-eq 2 ]; then
	cat $arg1 | sed -e 's/\(system.*;\)/\/\/\1/g' >tmp.xml
	# add the line with theProg and observer
	cat tmp.xml | sed -e 's/\(<\/system>\).*/system theProg, FetchStage, InstructionCache, FullCache;\1/' >$tgtdir/uppaal_file.xml
fi


# now enter tgtdir to perform the computations
cd $tgtdir

# record states explored
resfile=res.txt
echo -e  "-- Summary file -- Strategy is $stratname\n" >$resfile
echo -e "____________________________________________________________________" >>$resfile
printf  "|%10s %s %16s %s %12s %s %18s %s\n" "Iteration" "|" "Explored/Stored" "|" "trace length" "|" "Obs #states/#edges" "|" >>$resfile
echo -e "|-----------|------------------|--------------|--------------------|" >>$resfile

if [ $strategy	-eq 2 ]; then
	# use exact cache; no need to loop
	#
	[ $debug -eq 1 ] && (echo -n "Computing sup GBL_CLK with UPPAAL")
	echo "sup { theProg.END } : GBL_CLK" >wcet.q
	verifyta -t0 -u uppaal_file.xml wcet.q >uppaal_output.res
	numStored=$(cat uppaal_output.res | grep stored |  awk '{print $5}')
	numExplored=$(cat uppaal_output.res | grep explored |  awk '{print $5}')

	thewcet=$(cat uppaal_output.res | grep GBL_CLK | awk '{print $3}')
	echo " [DONE]"
	echo "   WCET (sup GBL_CLK) is $thewcet"
	echo "   States explored: $numExplored"
	echo "   States stored: $numStored"

	echo -n "Computing witness trace for $thewcet with UPPAAL"
	# echo "E<> theProg.END" >reach.q
	echo "E<> theProg.END && GBL_CLK >= $thewcet" >reach.q
	verifyta -t0 uppaal_file.xml reach.q 2>&1 | grep Delay: -A4 | grep 'useful=1' | grep useful=1 | sed  -e 's/cacheoutput=0/cacheoutput=H/' -e 's/cacheoutput=1/cacheoutput=M/' | sed -e 's/.*PC=\([0-9]*\).*cacheLine=\([0-9]*\).*cacheoutput=\([HM]\).*/\1,\2,\3/' > trace.tra
	echo " [DONE]"

	tracesize=$(wc -l trace.tra | awk '{print $1}')
	printf "|%10s %s %16s %s %12s %s %18s %s\n" "0" "|" "$numExplored/$numStored" "|" "$tracesize" "|" "N/A" "|" >>$resfile
	echo -e "--------------------------------------------------------------------" >>$resfile
	cat $resfile
	exit 0
fi

# at most 100 refinements
for n in `seq 0 100`
do
	# create the iteration folder step and copy the uppaal file
	mkdir -p $n
	cd $n

	echo "Starting iteration $n of refinement process"
	[ $n -eq 0 ] && src_uppaal_file=../uppaal_file.xml
	[ $n -eq 0 ] || src_uppaal_file=../$((n-1))/uppaal_file.xml

	cp $src_uppaal_file uppaal_file.xml

	# to get clean traces from UPPAAL traces
	[ $debug -eq 1 ] && (echo -n "Computing sup GBL_CLK with UPPAAL")
	echo "sup { theProg.END } : GBL_CLK" >wcet.q

	# grab UPPAAL resu.initial
	verifyta -t0 -u uppaal_file.xml wcet.q >uppaal_output.res
	numStored=$(cat uppaal_output.res | grep stored |  awk '{print $5}')
	numExplored=$(cat uppaal_output.res | grep explored |  awk '{print $5}')

	thewcet=$(cat uppaal_output.res | grep GBL_CLK | awk '{print $3}')
	echo " [DONE]"
	echo "   WCET (sup GBL_CLK) is $thewcet"
	echo "   States explored: $numExplored"
	echo "   States stored: $numStored"

	echo -n "Computing witness trace for $thewcet with UPPAAL"
	# echo "E<> theProg.END" >reach.q
	echo "E<> theProg.END && GBL_CLK >= $thewcet" >reach.q

	# test line; this line outputs a line of the form: PC, cacheLine, {H or M}
	# new script is more robust and does not depend on order of the states in the line
	verifyta -t0 uppaal_file.xml reach.q 2>&1 | grep Delay: -A4 | grep 'useful=1' | grep useful=1 | sed  -e 's/cacheoutput=0/cacheoutput=H/' -e 's/cacheoutput=1/cacheoutput=M/' | sed -e 's/.*PC=\([0-9]*\).*cacheLine=\([0-9]*\).*cacheoutput=\([HM]\).*/\1,\2,\3/' > trace.tra
	echo " [DONE]"

	# echo "Trace is:"
	# cat trace.tra

	[ $debug -eq 1 ] && (echo -n "Generating reference trace")
	# generate the reference trace
	# for the cache model input, we remove the PC component of each triple in the trace
	# cut -d extracts field 1 in original and here we extract f2
	# cat trace.tra | cut -d',' -f1 | cache_model $arg2 > trace.ref
	cat trace.tra | cut -d',' -f2 | cache_model $arg2 > trace.ref
	[ $debug -eq 1 ] && (echo " [DONE]")

	[ $debug -eq 1 ] && (echo -n "Checking whether witness trace is compatible with reference trace")
	# compute largest common prefix
	# first extract fields 2 and 3 (as the new trace contains PC in the first field)
	cat trace.tra | awk 'BEGIN{FS=","}{print $2","$3}' > cacheTrace.tra

	diff -b cacheTrace.tra trace.ref >/dev/null

	# do not move echo before this line as $? checks the last command
	if [ $? -eq 0 ]; then
		 [ $debug -eq 1 ] && (echo " [DONE]") ;
		 printf "|%10s %s %16s %s %12s %s %18s %s\n" "$n" "|" "$numExplored/$numStored" "|" "$tracesize" "|" "N/A" "|" >>../$resfile
		 echo -e "--------------------------------------------------------------------" >>../$resfile
		 echo "Feasible witness trace found" ;
		 cat ../$resfile
		 exit 0
	fi

	[ $debug -eq 1 ] && (echo " [DONE]")
	echo "Witness trace is infeasible - Refining cache model"
	#  trace from model is infeasible; refine it
	#
	# build the automaton
	# get the length >= 1 of the largest common prefix
	# use cacheTrace now to compute the diff
	linedifferent=$(diff -b cacheTrace.tra trace.ref | head -n1 | sed 's/\([0-9][0-9]*\)[^0-9].*/\1/')

	head -n $linedifferent trace.tra > trace.red

	[ $debug -eq 1 ] && (echo -n "Computing refinement ")
	# generate dot file
	cat trace.red | check_inclusion all > trace-auto.aut
	cat trace-auto.aut | set_construction > trace.dot
	[ $minimize -eq 1 ] && minimize trace.dot
	# dot -Tpng trace.dot > trace.png

	# Pablo added this: don't know what to do with it
	# (Pablo:) I like to see the output in png format (This is what the command does).
	# I thought that the [ `uname` = "Linux" ] would make the command not to interfere with your flow in MacOs
	# cat trace.red | check_inclusion all | set_construction > trace.dot
	# [ `uname` = "Linux" ] && dot -Tpng trace.dot > trace.png

	[ $debug -eq 1 ] && (echo " [DONE]")

	if [ $strategy -eq 0 ]; then
		# new feature: generate union with previous automaton
		# generate the union automaton
		if [ $n -eq 0 ]; then
			cp trace.dot trace-union.dot
		else
			union_construction ../$((n-1))/trace-union.dot trace.dot >trace-union.dot
		fi

		[ $minimize -eq 1 ] && minimize trace-union.dot

		# now the UPPAAL version
		echo -n "Generating UPPAAL union refinement"
		cat trace-union.dot | gen_uppaal_union $concrete
		# add it to the uppaal_file
		add_union_observer observer.xml uppaal_file.xml
		echo " [DONE]"
	else
		# standard product
		# compute UPPAAL observer
		# cat trace.red | check_inclusion all | set_construction | gen_uppaal $concrete
		# gen_uppaal puts result in observer.xml
		echo -n "Generating UPPAAL product refinement"
		cat trace.dot | gen_uppaal $concrete

		# add the automaton to the uppaal model
		mv observer.xml observer-1.xml
		cat observer-1.xml | sed -e 's/node_final/infeasible/g' -e 's/endnode/unknown/g' >observer.xml
		add_observer observer.xml uppaal_file.xml observer_$n
		echo " [DONE]"
	fi

	# collect results in $resfile
	# echo "HERE" >>../$resfile
	# echo -e "$n    |   $numStates  |  0 " >>$resfile
	if [ $strategy -eq 0 ]; then
		obsEdges=$(cat trace-union.dot | grep "\->" | wc -l | awk '{print $1}')
		obsNodes=$(cat trace-union.dot | sed -e 's/node_/\'$'\nnode_/g' | grep "node_" | sed -e 's/\(node_[a-z]*[0-9]*\).*/\1/' | sort | uniq | wc -l | awk '{print $1}')
	else
		obsNodes=$(cat trace.dot | sed -e 's/node_/\'$'\nnode_/g' | grep "node_" | sed -e 's/\(node_[a-z]*[0-9]*\).*/\1/' | sort | uniq | wc -l | awk '{print $1}')
		obsEdges=$(cat trace.dot | grep "\->" | wc -l | awk '{print $1}')
	fi

	tracesize=$(wc -l trace.red | awk '{print $1}')
	printf "|%10s %s %16s %s %12s %s %18s %s\n" "$n" "|" "$numExplored/$numStored" "|" "$tracesize" "|" "$obsNodes/$obsEdges" "|" >>../$resfile
	# printf "%10s %s %16s %s %18s %s %15s %s\n" $n "|" "$numStates" "|" "$tracesize" "|" "$b" "|" >>../$resfile
	#
	# wait
	# read 

	# go back to the beginning
	cd ..

done
