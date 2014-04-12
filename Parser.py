forbidden_flags = {
	'NONE',
	'UNIQUE',
	'QUESTOR',
	'MALE',
	'FEMALE',
	'CHAR_CLEAR',
	'ATTR_RAND',
	'ATTR_CLEAR',
	'ATTR_MULTI',
	'FORCE_DEPTH',
	'UNAWARE',
	'FORCE_SLEEP',
	'FORCE_EXTRA',
	'GROUP_AI',
	'SEASONAL',
	'XXX11',
	'XXX12',
	'ONLY_GOLD',
	'ONLY_ITEM',
	'ATTR_FLICKE',
	'MULTIPLY',
	'MIMIC_INV',
	'XXX1',
	'XXX2',
	'XXX3',
	'XXX4',
	'XXX5',
	'XXX6',
	'XXX7',
	'XXX8',
	'XXX9',
	'MOVE_BODY',
	'KILL_BODY',
	'NONLIVING'
}

mon_flags = {
	'INVISIBLE',
	'COLD_BLOOD',
	'NEVER_BLOW',
	'NEVER_MOVE',
	'DROP_40',
	'DROP_60',
	'DROP_1',
	'DROP_2',
	'DROP_3',
	'DROP_4',
	'DROP_GOOD',
	'DROP_GREAT',
	'DROP_20',
	'REGENERATE',
	'PASS_WALL',
	'KILL_WALL',
	'TAKE_ITEM',
	'KILL_ITEM',
	'TAKE_ITEM',
	'KILL_ITEM',
	'ORC',
	'TROLL',
	'GIANT',
	'DRAGON',
	'DEMON',
	'RAND_25',
	'RAND_50',
	'UNDEAD',
	'EVIL',
	'ANIMAL',
	'METAL',
	'HURT_LIGHT',
	'HURT_ROCK',
	'HURT_FIRE',
	'HURT_COLD',
	'IM_ACID',
	'IM_ELEC',
	'IM_FIRE',
	'IM_COLD',
	'IM_POIS',
	'IM_WATER',
	'NO_FEAR',
	'NO_STUN',
	'NO_CONF',
	'NO_SLEEP'
}

spell_flags = {
	'SHRIEK',
	'BR_ACID',
	'BR_ELEC',
	'BR_FIRE',
	'BR_COLD',
	'BR_POIS',
	'BR_LIGHT',
	# 'BR_DARK',
	'BR_CONF',
	'BR_INER',
	# 'BR_GRAV', //gravity
	'BA_ACID',
	'BA_ELEC',
	'BA_FIRE',
	'BA_COLD',
	'BA_POIS',
	'BA_WATE',
	# 'BA_DARK',
	'DRAIN_MANA',
	'BO_ACID',
	'BO_ELEC',
	'BO_FIRE',
	'BO_COLD',
	'BO_POIS',
	'BO_WATE',
	'BO_PLAS',
	'BO_ICEE',
	'SCARE',
	'BLIND',
	'CONF',
	'SLOW',
	'HOLD',
	'HASTE',
	'HEAL',
	'BLINK',
	'TPORT',
	# 'TELE_TO',
	# 'TELE_AWAY',
	# 'TELE_LEVEL',
	# 'DARKNESS',
	'TRAPS',
}

class MonsterTemplates(object):

	def __init__(self, name):
		self.name = name         # N : template name
		self.character = 0       # G : default display character
		self.painMessageID = 0   # M : pain message indexmo
		self.flags = []          # F : flag | flag | ...
		self.spell_flags = []    # S : spell flag | spell flag | ...
		self.description = ''    # D : description

	def __str__(self):
		return self.name + '\n\tchar : ' + chr(self.character) + '\n\tpain : ' + str(self.painMessageID) + '\n\tflags: ' + str(self.flags)	+ '\n\tspell: ' + str(self.spell_flags)

class Monster(object):

	def __init__(self, id, name):
		# N: serial number : monster name
		self.id = id
		self.name = name
		self.blows = []

	def __str__(self):
		f = set(self.flags) & mon_flags
		f = '|'.join(f)	
		s = self.spell_frequency + '@' + '|'.join(set(self.spell_flags) & spell_flags) if (self.spell_flags and set(self.spell_flags) & spell_flags) else ''
		b = '@'.join(self.blows) if self.blows else ''

		#'(id, name, temp, depth, rarity, expKill, speed, hit_points, armor_class, alertness, blows, flags, spells, description)'
		return '({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {})'.format(self.id, self.name, self.temp, self.depth, self.rarity, self.expKill, self.speed, self.hit_points, self.armor_class, self.alertness, b, f, s, self.description)

	def setTemplate(self, temp):
		self.temp = temp.name;
		self.flags = temp.flags[:]
		self.spell_flags = temp.spell_flags[:]
		self.description = temp.description

	def setInformation(self, info):
		# I: speed : hit points : vision : armor class : alertness
		self.speed = int(info[0])
		self.hit_points = int(info[1])
		self.armor_class = int(info[2])
		self.alertness = int(info[3])

	def setMoreInformation(self, info):
		# W: depth : rarity : unused (always 0) : experience for kill
		self.depth = int(info[0])
		self.rarity = int(info[1])
		self.expKill = int(info[3])

	def setBlows(self, info):
		# B: attack method : attack effect : damage
		self.blows.append('|'.join(info))

	def setSpellFrequency(self, info):
		# S: spell frequency
		self.spell_frequency = info

	def setSpell(self, info):
		# S: spell type | spell type | etc
		self.spell_flags.extend(info)

	def setFlags(self, info):
		# F: flag | flag | etc
		self.flags.extend(info)

	def unsetFlags(self, info):
		# -F: flag | flag | etc
		self.flags = set(self.flags) - set(info)

	def setDescription(self, info):
		# D: Description
		self.description += info + ' '


# Pars monster templates #
Templates = {}
with open('monster_base.txt', "r") as base:
	lines = base.readlines()
	temp = 0
	for line in lines:
		if line[0] == '#':
			continue
		elif line[0] == 'N':
			temp = MonsterTemplates(line.split(':')[1].strip())
		elif line[0] == 'G':
			temp.character = ord(line.strip().split(':')[1])
		elif line[0] == 'M':
			temp.painMessageID = int(line.strip().split(':')[1])
		elif line[0] == 'F':
			temp.flags.extend(line.split(':')[1].strip().split('|'))
		elif line[0] == 'S':
			temp.spell_flags.extend(line.split(':')[1].strip().split('|'))
		elif line[0] == 'D':
			temp.description = line.split(':')[1].strip() + '. '
		elif line[0] == '\n' and temp != 0:
			Templates[temp.name] = temp
			temp = 0
	if temp != 0:
		Templates[temp.name] = temp

monsters = []
with open('monster.txt', "r") as base:
	lines = base.readlines()
	monster = 0
	for line in lines:
		info = line.split(':')
		if line[0] == '#':
			continue
		elif line[0] == 'N':
			monster = Monster(int(info[1]), info[2].strip())
			set_spell_frequency = False 

		elif line[0] == 'T':
			 monster.setTemplate(Templates[info[1].strip()])

		elif line[0] == 'G':
			pass
		elif line[0] == 'C':
			pass
		elif line[0] == 'I':
			monster.setInformation(info[1:])

		elif line[0] == 'W':
			monster.setMoreInformation(info[1:])

		elif line[0] == 'B':
			monster.setBlows(list(map(lambda x: x.strip(), info[1:])))

		elif line[0] == 'S' and not set_spell_frequency:
			set_spell_frequency = True
			monster.setSpellFrequency(info[1].strip())

		elif line[0] == 'S':
			monster.setSpell(info[1:])

		elif line[0] == 'F':
			monster.setFlags(info[1].split('|'))

		elif info[0] == '-F':
			monster.unsetFlags(info[1].split('|'))

		elif line[0] == 'D':
			monster.setDescription(info[1].strip())

		elif info[0] == 'friends' or info[0] == 'drop' or info[0] == 'drop-artifact' or info[0] == 'mimic':
			pass

		elif line[0] == '\n' and monster != 0:
			monsters.append(monster)
			monster = 0
	if monster != 0:
		monsters.append(monster)
		
print('INSERT INTO mobs(id, name, temp, depth, rarity, expKill, speed, hit_points, armor_class, alertness, blows, flags, spells, description) VALUES')
for mob in monsters:
	if not (set(mob.flags) & forbidden_flags) and (set(mob.flags) & mon_flags):
		print(mob)