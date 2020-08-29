def extract_word(code):
    import encoding
    import bases

    index_pos = bases.to_ten(code, encoding.compression)

    return _words[index_pos]

def word_index(word):
    import encoding
    import bases
    if word in _words:
        return bases.from_ten(_words.index(word), encoding.compression)
    else:
        return -1
    
_words = """the
and
was
his
for
had
you
not
but
all
him
she
are
one
who
out
any
has
our
man
may
now
can
did
two
see
old
men
own
day
how
way
que
say
too
def
get
die
yet
saw
put
new
und
off
few
son
far
got
set
let
qui
nor
pos
use
end
den
pas
une
thy
war
met
why
lay
air
sat
boy
par
ten
sur
ask
law
lui
est
das
sea
van
six
von
age
sir
art
sie
act
ich
cut
eye
bad
low
mit
pay
run
dem
sun
big
fld
het
ist
ran
als
col
ein
try
auf
arm
ago
dit
een
hat
fit
etc
joy
sit
los
mon
och
due
top
lie
fee
bit
cry
hot
dat
ill
eat
sin
aux
non
wie
del
tax
car
lot
dog
con
sad
sky
dry
add
aid
che
las
mir
por
oli
som
nur
ear
aus
fut
box
qex
moi
buy
sum
qau
han
won
ihm
ils
ihr
det
mad
cup
tea
ihn
una
beg
att
aan
fat
wir
wit
bow
gay
win
lad
wet
ice
bei
nos
spn
uns
ere
hem
gun
vor
vol
god
key
oil
dim
hit
vie
ont
zum
web
fix
odd
cap
apt
cet
pen
rid
ate
til
bid
var
sex
era
aim
bon
row
hid
lit
bar
kun
dan
owe
iii
tot
roi
bay
nun
leg
sig
bag
sus
ton
cat
fun
zur
sen
job
jag
har
mud
gas
hun
nom
uit
inn
bin
dig
nay
zoo
mot
dir
nyt
vos
foe
nog
hut
bis
ete
oft
mas
ans
tie
oak
ole
kan
net
ook
fro
vom
deg
sei
tin
dos
hay
ici
egg
fur
lip
mob
raw
rue
woe
fin
pot
viz
adv
wat
ami
bet
mig
map
din
log
plw
ben
gar
cow
mis
awe
sah
ons
imp
wel
shy
dis
hen
pit
lap
fog
dug
toi
git
rod
sue
zou
pan
ett
don
tan
min
gut
spy
vow
paa
wuz
ass
kam
cab
duc
ftp
wax
ray
mix
cum
ang
hue
dew
ham
vue
axe
yea
bee
bas
rob
vit
dix
jos
saa
ink
kin
hon
grk
fox
toe
ins
gli
syn
tip
nie
jeg
pet
pig
mai
plu
adj
eve
doe
muy
sua
pub
pin
wid
nod
hoe
fra
cas
yon
wee
pie
fan
jar
foi
lib
jaw
asp
icy
heb
oui
kom
rub
voi
hin
zyn
ken
suo
wan
tel
vii
uno
dey
kid
ore
ale
sic
loi
sob
via
dip
nel
rug
vin
rim
dar
zag
toy
ark
bat
kon
upp
ble
tap
bud
lbs
mag
rat
cri
com
wad
wil
mhw
wig
tun
jam
piu
vis
esp
vel
sub
dam
mat
rot
owl
dei
chi
vez
oar
gum
xii
aut
rag
zei
tai
ash
aft
fue
ivy
sup
pur
nut
mio
gin
tre
dus
cui
dia
cru
hee
lat
vad
sap
hoc
gan
poi
jug
vil
nec
gem
tub
mia
dye
vas
oss
ebb
ram
quo
tog
vex
kai
dad
fry
hos
ant
ape
bug
mee
jet
dot
dun
tar
eke
lee
fou
cot
ado
nez
nam
cou
ous
seq
tut
elk
alt
tug
zog
hev
qua
mur
gik
tho
paw
bye
hab
ind
fol
goe
fir
sai
mun
hip
tra
zig
ego
pat
ane
zip
voz
ain
gaf
mil
pew
cit
bel
aun
err
sot
nap
xiv
xvi
fig
arc
tow
that
with
have
from
this
were
they
said
been
will
them
when
more
into
your
what
yeah
than
some
very
upon
time
like
then
made
such
must
much
know
good
down
most
came
come
make
long
work
even
many
well
life
went
back
same
last
take
here
just
also
away
hand
part
left
took
once
each
face
whom
tell
love
head
find
both
told
look
mind
knew
seen
days
name
done
full
gave
soon
want
side
home
till
thou
best
felt
word
dans
next
less
door
poor
sent
room
half
keep
kind
form
pour
near
thus
year
read
true
wife
high
says
dear
fact
case
hear
four
hope
sure
open
feet
wish
gone
girl
held
help
free
vous
land
body
thee
call
hour
feel
rest
plus
lost
kept
care
fire
mean
fell
dead
hard
soul
sort
fine
hold
lady
turn
town
show
fear
dark
talk
paid
arms
idea
book
seem
avec
copy
meet
deep
line
sich
rose
laws
past
nous
none
else
late
laid
cold
view
fair
army
note
fall
pass
road
elle
send
hair
glad
hath
bien
play
tout
wild
save
gold
mark
bear
wind
mine
drew
mais
foot
unto
rich
mere
lips
blue
easy
ship
stay
grew
born
http
boys
walk
cost
evil
main
food
wide
week
boat
sans
date
wait
tree
cast
tone
step
news
fast
wall
soft
race
lord
wise
plan
rise
rate
eine
pain
wood
vain
pale
lead
stop
warm
vast
sake
loss
shot
ways
spot
camp
move
fait
pure
safe
iron
rule
page
lose
sign
seat
path
auch
wine
shut
seek
tall
dare
hung
ears
sick
game
file
fond
draw
grow
hill
bank
tous
sons
kill
thin
fate
sont
hall
bent
neck
hast
rock
bore
noch
list
blow
weak
pray
rain
poet
fish
nine
rode
song
gate
wore
beat
calm
dont
mass
heat
nach
snow
rank
edge
loud
moon
west
busy
size
nice
pair
gods
slow
hurt
niet
join
mich
task
fool
post
sold
legs
bird
fort
huge
dass
east
coat
firm
ride
wear
sein
soil
gain
fail
hate
site
mile
bare
drop
rare
donc
dull
text
gray
lest
sous
suit
term
dust
nose
tale
fill
cool
dire
wenn
worn
salt
bold
rien
ease
lake
sell
roof
base
dogs
milk
meat
cela
skin
flat
gift
guns
sail
bill
suis
alle
band
ends
sang
lack
disk
kiss
ruin
maid
hide
ball
doth
self
sand
harm
rage
shop
risk
male
grey
poem
fois
aunt
host
doch
noon
keen
fame
gaze
hero
para
amid
jour
niin
baby
corn
mode
sind
hole
eggs
deck
dawn
farm
bade
rude
deny
voor
wird
port
bell
flew
silk
vote
rush
idle
rear
sank
kuin
chez
pipe
luck
sole
voir
brow
faut
hang
como
owns
sigh
tide
trop
stir
runs
tent
haar
cent
debt
puis
denn
cook
obey
hell
crew
zich
lamp
pull
tail
torn
oath
star
mood
folk
ours
sins
card
mehr
wave
tiny
coup
rang
burn
miss
lift
meme
naar
flow
maar
pick
flag
yard
roll
hart
rope
tear
inch
wilt
trip
test
tant
ihre
sale
fury
cure
mein
root
data
pace
voix
slip
veil
duke
club
wire
soit
sunk
mild
pays
alas
papa
wars
kann
rent
hint
tour
area
glow
sore
roar
bark
wake
quit
seas
wept
jest
pink
desk
dine
nest
wont
leaf
blew
cave
plot
lion
joka
habe
mail
dann
heap
mist
mort
chin
sehr
wash
grim
pile
beds
rays
dish
toil
vice
wohl
hunt
eene
knee
joke
sink
alla
prey
pack
envy
tomb
load
ganz
sais
nuit
acid
foul
ohne
sera
lean
coin
lend
robe
mate
void
clay
push
heir
tore
bras
haue
ward
lock
dost
dich
gens
gown
sung
neat
avez
bend
pine
bone
quel
asks
weep
park
fain
rice
icke
lent
adds
dumb
beau
fund
damp
bald
etre
soir
jury
whip
fils
tend
cell
chap
jump
rows
palm
mill
sofa
hans
leap
warn
clad
pris
fare
shew
dame
loin
vent
nigh
viel
vile
drag
sage
herd
vrai
bush
bars
lace
earn
bull
arch
bond
gang
akin
belt
joys
este
woke
cart
bout
bulk
barn
horn
tune
ikke
stem
bath
weil
quoi
crop
boil
lads
doit
beef
cake
fist
geen
bind
ripe
bowl
urge
sums
cash
navy
bows
rosy
wolf
hade
dash
pool
cups
pole
cars
vaan
heel
todo
bite
gale
trap
mask
sire
monk
keys
esta
kein
echo
slew
code
rail
soup
lawn
lazy
cinq
pint
wool
slay
soll
dusk
tops
deaf
lane
cows
earl
halt
amis
waar
bury
ciel
swim
daar
tons
limb
dirt
mois
grip
doom
sens
prit
deze
airs
lays
pero
mare
erst
hail
sway
haut
tete
lang
quod
afar
mute
hire
lain
team
cage
beam
sack
oven
cane
mock
pony
cord
fold
rash
tube
logs
werd
oars
deem
sait
lame
eben
kick
oxen
dort
vols
diet
yoke
bois
tame
zwei
gulf
lust
hare
verb
pomp
rags
vais
knot
lamb
writ
duel
fuel
vows
bags
flee
font
drum
jail
jaws
link
mule
plea
gilt
deja
toen
hook
morn
hymn
vary
docs
joie
slid
sade
vein
anew
dice
mess
bolt
loan
pere
pond
cote
odor
idol
ouml
muss
slim
cour
mari
onze
aims
shoe
turf
vois
swam
moss
odds
tact
gran
anon
dome
airy
tard
foam
vint
vnto
toch
ella
hush
lump
sown
dien
veel
pang
zone
dove
pigs
spur
chef
fork
defy
sect
melt
sour
nail
gros
cock
ovat
casa
olla
geht
bids
pope
edit
role
doen
vite
tire
lime
epic
tint
vale
mast
pork
ribs
tool
cosa
gait
knit
quil
wage
gems
boss
solo
pose
dein
tray
sane
sagt
bake
fade
zwar
cref
damn
loaf
isle
maps
olen
hors
fuss
trot
onto
quam
lone
feat
acre
lass
unit
sino
modo
tuli
rods
haze
mien
howl
koko
bust
taas
kwam
otro
aura
dose
dazu
alte
tips
peep
noir
toda
drei
nahm
gout
mcol
brig
bist
rack
toss
nuns
reap
sunt
fais
surf
rung
crow
hood
soda
leva
toys
snug
drug
nang
rend
wink
spun
hawk
core
otra
oaks
vida
ware
liar
wail
sine
furs
sina
pump
twin
jars
buds
cape
aime
snap
vase
seit
alms
paix
thei
spit
doux
dart
peas
mano
beim
vara
butt
fand
moan
pins
limp
jene
arme
inte
bons
blot
doll
pois
bait
shun
coil
kaum
gaan
cris
trat
lire
prix
vill
zien
boom
lash
soin
bene
puff
owne
exit
gear
gibt
whim
ache
clan
womb
ford
tank
dois
germ
lava
sitt
spin
vide
jeta
frog
muse
comb
utan
bang
glee
agin
rout
sept
gasp
sham
omen
vest
lore
cada
wary
juge
fret
raid
weet
nook
bony
haul
mice
auld
rite
ills
fini
jerk
span
nave
jota
allt
sate
wrap
curl
wand
curb
dint
beak
yore
lids
zelf
lark
abbe
mane
moor
fern
pail
bass
wast
brim
myth
axis
arid
buck
gute
hens
undo
rust
hull
hvad
ojos
film
fete
quid
wins
yarn
fuhr
atom
cite
gust
etwa
loop
stad
roam
cove
pans
scar
paar
lull
fera
zest
whit
lick
cope
lois
riep
pike
lure
thet
fiel
bays
bard
hogs
pens
idee
cant
cria
pref
boar
enim
gern
rapt
maze
lute
cafe
siis
drie
prie
herb
mire
stod
balm
glen
voll
chop
bide
stab
oben
fray
weel
oude
vont
zinc
cork
clap
nada
mise
tort
hoop
jobs
wist
jede
mens
olet
dewy
lief
sash
hazy
paws
keel
hebt
darf
quay
gall
toll
slab
begs
hilt
mitt
rake
slap
loom
twig
cest
suck
xiii
loue
levy
hace
cone
fram
hose
vita
stag
pont
suns
reel
jist
eaux
elms
moth
aide
thar
amor
lors
demi
mace
sois
tidy
fizz
buzz
hack
naam
bort
mair
stew
figs
rugs
halo
huis
which
their
would
there
could
about
great
these
first
where
those
shall
might
every
think
found
still
while
again
place
young
years
three
right
house
whole
world
night
heard
heart
among
small
woman
whose
quite
words
given
taken
hands
until
since
light
began
large
works
often
stood
money
means
round
voice
white
point
state
above
death
least
known
along
leave
alone
women
speak
forth
terms
child
human
short
cause
seems
doubt
black
sense
close
truth
ought
ready
force
earth
sight
spoke
story
stand
nicht
hours
happy
clear
sound
blood
comme
avait
front
quill
peace
horse
wrote
cette
chief
books
visit
heavy
knows
carry
plain
sweet
write
below
wrong
reach
noble
agree
enemy
worth
green
third
mouth
sleep
fresh
faith
smile
bound
quiet
etext
court
youth
piece
meant
seven
tears
value
broke
fight
stone
begin
learn
grand
month
girls
eight
scene
drawn
field
chair
allow
music
study
spent
trust
break
threw
watch
looks
built
spite
walls
touch
steps
dress
grave
shown
board
faire
glass
share
forms
class
start
shook
train
prove
floor
worse
sorry
pride
marry
crowd
shore
drink
judge
serve
north
laugh
trade
broad
grace
heads
proud
space
quick
ideas
fancy
taste
sword
ships
glory
brave
honor
dream
weeks
south
thick
claim
check
aside
false
cross
sharp
hills
bread
coast
aware
group
grown
birds
midst
tells
throw
habit
stage
angry
brown
grass
twice
guard
burst
price
thank
guess
chose
uncle
woods
drove
title
minds
noise
begun
cruel
shape
slave
birth
yours
storm
catch
loose
civil
dozen
shows
admit
smoke
brief
proof
flesh
fruit
march
enjoy
worst
shame
rough
rocks
crime
grief
needs
lands
blind
brain
clean
durch
etait
level
raise
stars
faint
teeth
labor
rooms
einen
mercy
avoid
doors
devil
weary
arose
fault
crown
color
aussi
event
goods
quand
yards
temps
favor
votre
souls
calls
ahead
plans
views
signs
teach
count
stock
kinds
apart
queen
guide
exact
homme
toute
hotel
spend
skill
kings
shalt
links
dance
swept
wound
store
slept
range
hatte
hence
image
arise
einem
banks
rapid
waste
entre
plant
shade
cloud
press
wings
flung
thine
stick
reign
print
fleet
turns
sugar
spare
solid
blame
borne
towns
bosom
corps
grant
finds
moins
build
error
cease
delay
agent
cloth
issue
charm
treat
email
frame
sheep
alike
pause
seine
merry
femme
vague
trace
verse
notre
flame
union
avoir
haste
route
dread
exist
stern
falls
yield
occur
ocean
shake
staff
hurry
beast
trois
shone
drank
winds
cheek
chain
knife
fears
swift
widow
holds
merit
gross
diese
angel
ainsi
roads
swear
nurse
cabin
marks
tribe
aloud
pains
alarm
walks
folks
match
wrath
dwell
slain
waren
coach
deeds
haben
steel
trail
depth
bears
porte
shock
guest
schon
plate
saith
feels
clerk
stuff
track
poems
plays
magic
keeps
monde
songs
leads
drunk
awake
shoot
smell
petit
altar
autre
limbs
eaten
basis
steep
feast
novel
grows
stout
risen
crept
scale
ranks
flash
burnt
bride
chest
grain
prize
fetch
curse
stiff
beard
negro
assez
model
grasp
flood
alors
jours
flour
stole
pound
swung
sheet
dwelt
cloak
clock
score
gloom
agony
seize
pious
blows
wheel
sagte
avant
scorn
abide
stuck
shout
paint
ample
ruins
maybe
fille
siege
steam
mille
trick
shell
giant
lords
fifth
meine
linen
vivid
straw
brass
bonne
sport
drops
beach
celui
cards
saint
cheap
trunk
major
joint
spell
abode
drama
array
craft
canoe
gegen
rifle
fiery
awoke
limit
ghost
bells
climb
heels
frank
terre
mutta
harsh
shine
knock
peine
spake
virus
dealt
minor
slope
humor
bench
wheat
mamma
piano
funny
rocky
madam
sweep
erect
belle
purse
mount
parce
clung
swore
fence
worry
suite
wagon
evils
celle
jetzt
bands
chase
gleam
mains
lucky
peril
extra
doute
tight
blank
enfin
seeds
fairy
smart
etwas
wurde
shirt
elect
spoil
guilt
adopt
bills
sails
rings
blown
ridge
dense
today
abuse
reste
quoth
lunch
niece
timid
sworn
hello
prime
prose
ville
creek
organ
brush
opera
flock
boast
pitch
angle
sunny
aught
fools
swamp
veins
couch
opens
spear
brick
cream
thief
waist
knelt
brute
sends
paths
shops
porch
ihren
backs
sandy
pupil
lodge
bloom
bonds
block
swell
rigid
ollut
scent
heeft
cigar
entry
avail
effet
tread
olisi
chill
Fizz
Buzz
FizzBuzz
Hello
World
Vyxal
vyxal
forth
feel
looking
following
rest
terms
business
electronic
behind
making
really
friends
towards
different
understand""".split("\n")

if __name__ == "__main__":
    while 1:
        x = input()
        print(word_index(x))
