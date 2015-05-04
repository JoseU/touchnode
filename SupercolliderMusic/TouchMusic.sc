s.boot;
NetAddr.langPort;


(
OSCdef.new(
	\freq1,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\gate1, msg[1]);

	},
	'/medialab/toggle1'
);

OSCdef.new(
	\freq2,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\gate2, msg[1]);

	},
	'/medialab/toggle2'
);

OSCdef.new(
	\freq3,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\gate3, msg[1]);

	},
	'/medialab/toggle3'
);

OSCdef.new(
	\freq4,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\gate3, msg[1]);

	},
	'/medialab/toggle4'
);


OSCdef.new(
	\freq5,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\gate4, msg[1]);

	},
	'/medialab/toggle4'
);


OSCdef.new(
	\freq6,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\gate4, msg[1]);

	},
	'/medialab/toggle4'
);







OSCdef.new(
	\fader1,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\freq1, msg[1].linexp(0, 1, 20, 300));
	},
	'/medialab/fader1'
);

OSCdef.new(
	\fader2,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\freq2, msg[1].linlin(0,1,5,400));
	},
	'/medialab/fader2'
);

OSCdef.new(
	\fader3,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\freq3, msg[1].linexp(0,1,10,80));
	},
	'/medialab/fader3'
);


OSCdef.new(
	\fader4,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\freq4, msg[1].linexp(0,1,3,15));
	},
	'/medialab/fader4'
);


OSCdef.new(
	\fader5,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\freq5, msg[1].linexp(0,1,6000,10000));
	},
	'/medialab/fader5'
);




OSCdef.new(
	\fader6,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\freq6, msg[1].linexp(0,1,100,1000));
	},
	'/medialab/fader6'
);




OSCdef.new(
	\rotary1,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\detune, msg[1].linlin(0,1,0.01,12));
	},
	'/medialab/rotary1'
);

OSCdef.new(
	\rotary2,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\detune, msg[1].linexp(0,1,0.01,12));
	},
	'/medialab/rotary2'
);

OSCdef.new(
	\rotary3,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\vel, msg[1].linlin(0,1,50,100));
	},
	'/medialab/rotary3'
);

OSCdef.new(
	\rotary4,
	{
	arg msg, time, addr, port;
		msg[1].postln;
		x.set(\vel, msg[1].linexp(0,1,50,100));
	},
	'/medialab/rotary4'
);

)



(
SynthDef(\impulso, {
	arg out=0, freq1=20, freq2=5, freq3=10, freq4=3, freq5=6000, freq6=100,
	gate1=1, gate2=1, gate3=1, gate4=1,

	amp=1, nharm=12, detune=0.2, pan=0, vel=1;

	var sig, sig1, env1, sig2, env2, sig3, env3, sig4, env4;

	env1 = EnvGen.kr(Env.adsr(0.05,0.1,0.5,3), gate1);
	env2 = EnvGen.kr(Env.adsr(0.05,0.1,1,4), gate2);
	env3 = EnvGen.kr(Env.adsr(0.05,0.1,1,4), gate3);
	env4 = EnvGen.kr(Env.adsr(0.05,0.1,1,4), gate4);

	sig1 = Dust.ar(freq1, 0.2);
	sig2 = Resonz.ar(LFNoise0.ar(freq2),Line.kr(10000,200,10),0.1);
	sig3 = LFTri.ar(freq3,0,1)*LPF.ar(Pulse.ar(freq4));
	sig4 = BPF.ar(Dust.ar(200), freq5, 60/freq6);

	sig = sig2*env2+sig3*env3+sig4*env4;
	Out.ar(out, sig);
}).play;
)

x = Synth.new(\impulso);
x.set(\velocidad, 4);




(
SynthDef.new(\user1, {
	arg freq=440, atk=0.005, rel=0.3, amp=0.1, pan=0, gateuser=1;
	var sig, env;
	sig = SinOsc.ar(freq);
	env = EnvGen.kr(Env.new([0,1,0], [atk,rel], [1, -1]), gateuser, doneAction:2);
	sig = Pan2.ar(sig, pan, amp);
	sig = sig * env;
	Out.ar(0, sig);
	}).add;
)


z = Synth.new(\user1);
z.set(\gateuser, 0);





a=[[60,62,64,67].wchoose([0.5, 0.25, 0.20, 0.05])]


(
Pdef(
	\sinepat,
    Pbind(
		\instrument, \user1,
		\dur, Pwhite(0.05, 0.5, inf),  // modificar por osc, tempo
		\midinote, Pseq(a, inf).trace,
		\harmonic, Pexprand(1, 80, inf).trace,
		\atk, Pwhite(0.01, 1.0, inf).trace,   // modificar por osc de acuerdo a la cercanía
		\rel, Pwhite(0.1, 0.5, inf),
		\amp, Pkey(\harmonic).reciprocal * 0.1,
		\pan, Pwhite(-0.8, 0.8, inf)
	);
).play;
)



(
Pdef(
	\sinepat).stop;
)



{Dust.ar(MouseX.kr(20,300), 0.2)}.play  //1 variable
{Resonz.ar(LFNoise0.ar(MouseX.kr(5,400)),Line.kr(10000,200,10),0.1)}.play //1 variable
{LFTri.ar(MouseY.kr(10,80),0,1)*LPF.ar(Pulse.ar(MouseX.kr(3,15)))}.play  //2 variables
{BPF.ar(Dust.ar(100), MouseX.kr(6000,10000), 60/MouseY.kr(100,1000))}.play //2 variables
