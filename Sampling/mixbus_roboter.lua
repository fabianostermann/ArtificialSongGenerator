#### Use this for importing files to the editor (should remove all existing regions beforehand)

-- ardour { ["type"] = "Snippet", name = "Import File(s) Example" }

function factory (params) return function ()
	Editor:select_all_tracks ()

	local files = C.StringVector()

	files:push_back("C:/Users/oyster/Dropbox/WHF/ArtificialSongGenerator_workspace/Sampling/Database-ISMIR2020/midi+rawaudio-250/0001_TenorSax.mid")

	local pos = -1
		Editor:do_import (files,
			Editing.ImportDistinctFiles, Editing.ImportToTrack, ARDOUR.SrcQuality.SrcBest,
			ARDOUR.MidiTrackNameSource.SMFTrackName,
			ARDOUR.MidiTempoMapDisposition.SMFTempoIgnore, -- fatal error when loading tempo, new Mixbus 6 better?
			pos, ARDOUR.PluginInfo())

	
	track = Session:route_by_name("MIDI"):to_track()
	print(track:name())
	print(track:is_selected())
	
	print("Exit.")

end end

### Use this for removing regions


### Use this for opening export dialog and calling os functions (could be a robot util for pressing enter!)

---- this header is (only) required to save the script
-- ardour { ["type"] = "Snippet", name = "" }
function factory () return function ()

print ("start os execute")
local handle = io.popen("echo osecho")
local result = handle:read("*a")
print (result)
handle:close()

print("start export")
Editor:export_audio()

print ("Exit.")

 end end
 
### Under Linux: xdotool is used to trigger export, working!
 
---- this header is (only) required to save the script
-- ardour { ["type"] = "Snippet", name = "" }
function factory () return function ()

	print ("start os execute")
	local handle = io.popen("sleep 3; xdotool key Return")

	print("start export")
	Editor:export_audio()

	local result = handle:read("*a")
	print (result)
	handle:close()

print ("Exit.")

end end




##!! all GUI shortcuts
http://mixbus.harrisonconsoles.com/forum/thread-4332.html



#### best version for now!
function factory (params) return function ()

	Editor:access_action("Editor","select-all-objects")
	local sel = Editor:get_selection ()
	for r in sel.regions:regionlist ():iter () do
		if r:isnil() then goto next end
		print (r:name())--"Pos:", r:position(), "Start:", r:start())
		Editor:access_action("Region","remove-region")
		::next::
	end
	collectgarbage ()

	Editor:select_all_tracks ()

	local files = C.StringVector()

	files:push_back("/home/oyster/Dropbox/WHF/ArtificialSongGenerator_workspace/Sampling/Database-ISMIR2020/midi+rawaudio-250/0001_TenorSax.mid")

	local pos = -1
		Editor:do_import (files,
			Editing.ImportDistinctFiles, Editing.ImportToTrack, ARDOUR.SrcQuality.SrcBest,
			ARDOUR.MidiTrackNameSource.SMFTrackName,
			ARDOUR.MidiTempoMapDisposition.SMFTempoUse, -- fatal error when loading tempo, new Mixbus 6 better?
			pos, ARDOUR.PluginInfo())

	track = Session:route_by_name("MIDI"):to_track()
	print(track:name())
	print(track:is_selected())

	print ("start os execute")
	local handle = io.popen("sleep 3; xdotool key Return")
	print("start export")
	Editor:export_audio()
	local result = handle:read("*a")
	print (result)
	handle:close()
	
	print ("renaming..")
	os.rename("/media/oyster/Audio/MixBus Projects/RoboterTestspace/export/session.flac", "/media/oyster/Audio/MixBus Projects/RoboterTestspace/export/renamed.flac")
	
	print("Exit.")
end end



