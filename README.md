Pipe Goggles
===========

Description
---------------------------

Pipe Goggles currently only adds a single item, the Pipe Goggles.
They allow the wearer (just hold it in your hand) to see various plumbing through the walls.

Which pipes are visible can be configured by right-clicking the tool.

Currently supported mods (1.14):
--------------------------------

- Refined Storage
- Simple Storage Network
- Inspirations
- Integrated Dynamics

Currently supported mods (1.12):
--------------------------------

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

Adding support for other mods:
---------------------------

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
