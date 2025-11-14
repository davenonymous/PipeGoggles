# Pipe Goggles

## Description

Pipe Goggles currently only adds a single item, the Pipe Goggles.
They allow the wearer to see various plumbing through the walls.

## Usage

Right click with the goggles in hand to open a small configuration GUI. 

![GUI](https://github.com/davenonymous/PipeGoggles/blob/1.21.1/assets/goggles-gui.png?raw=true)

Insert the pipes you want to see into the colored slots.

The goggles can be in three different modes:

- Always on: The goggles are always active, showing all pipes.
- Always off: The goggles are never active, showing no pipes.
- Automatic: The goggles will only show matching pipes when the player is holding a pipe item in their hand.

You can additionally configure the range of the goggles, the line-thickness of the pipes and their opacity.

Depending on the configuration of the mod your goggles will need to be powered to work!

## Supported mods (1.21.1):

- Applied Energistics 2
- Create
- EnderIO
- Flux Networks
- IntegratedDynamics
- LaserIO
- Mekanism
- Redstone (from vanilla minecraft)
- Modern Industrialization
- MoreRed
- Oritech
- Pipez
- PneumaticCraft
- Refined Storage
- Replication
- XNet


## Supported mods (1.14):

- Refined Storage
- Simple Storage Network
- Inspirations
- Integrated Dynamics

## Supported mods (1.12):

- Applied Energistics
- Cyclic
- EnderIO
- Extra Utilities 2
- Immersive Engineering
- Integrated Dynamics
- Mekanism
- OpenComputers
- Refined Storage
- Simple Storage Network
- Thermal Dynamics

## Adding support for other mods (1.21.1 and above):

This is pretty straight forward and an example is better than words for this one:
```json
{
  "neoforge:conditions": [
    {
      "type": "neoforge:mod_loaded",
      "modid": "xnet"
    }
  ],
  "blocks": [
    "xnet:netcable",
    "xnet:connector"
  ],
  "items": [
    "#xnet:cables",
    "#xnet:connectors"
  ],
  "mod": "xnet"
}
```

`blocks` are the blocks that should be highlighted. These can be block ids or tags.
`items` are the items that enable the goggles in auto-mode to highlight the pipes. Also item ids or tags.

That file should be placed here: `<your-data-pack>/data/pipegoggles/pipegoggles/mods/xnet.json`.

More examples can be found [here](https://github.com/davenonymous/PipeGoggles/tree/1.21.1/src/generated/resources/data/pipegoggles/pipegoggles/mods).


## Adding support for other mods (1.18 and below):

This is actually quite easy as only a single .json file per mod is required and it does not have many options.
It might be enough for you to just take a look at one of the shipped integrations, for example the one for [Thermal Dynamics](https://github.com/thraaawn/PipeMaster/blob/master/src/main/resources/assets/pipemaster/config/blockgroups/thermaldynamics.json).

You generally configure Block-Groups, i.e. collections of blocks belonging to a mod that should be highlighted.
Each block-group can be individually disabled in configs, which is one of the reasons why you need to give a unique id for blockgroups. If you are unsure what you should put here, simply use the modid again.

Here's a full summary of all the properties that can or should be set:

| Property | Description | Example |
| --- | --- | --- |
| id  | Name of this blockgroup, usually the mod id | ```mymod``` |
| mod | The mod id for this blockgroup. If the named mod is not loaded, neither will this blockgroup be. | ```mymod``` |
| itemIcon | Specify an item definition here, either as simple string or as complex json object. See below for more information on this. | ```minecraft:apple``` |
| translationKey | The translation key used to identify the button group to players. Can also be an already translated string, but using translation keys is recommended. If the mod provides a creative tab, that's usually the translation key you want to use. | ```itemGroup.mymod``` |
| blocks | An array of block definitions. All these blocks will be highlighted. This is not the name of the item you are holding, but the name of the placed block! Those are often times not the same. Make sure you are using the block names! | ```["mymod:cable", {"name": "mymod:controller", meta: 1} ]``` |
| optimizationStrategy | You probably won't need to change this setting. It is only necessary for some mods that are stacking their bounding boxes. If your pipes look weird or have open ends, try changing this value to ```SKIP_DUPLICATE_LINES```. The default is set to ```REMOVE_DUPLICATE_LINES```. | ```REMOVE_DUPLICATE_LINES``` |

Item/Block definitions can either be specified by simply giving the ResourceLocation, e.g. minecraft:apple.
Or by specifying a more complex json object, which is capable of passing on meta and nbt data:
```json
{ "name": "thermaldynamics:duct_0", "meta": 5 }
```

Pull Requests with new integrations are always welcome!

Please report any bugs you encounter!
