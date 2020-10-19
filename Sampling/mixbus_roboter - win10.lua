function file_exists(name)
   local f=io.open(name,"r")
   if f~=nil then io.close(f) return true else return false end
end

function set_solo(track, state)
	if (state) then
		track:solo_control():set_value(1, PBD.GroupControlDisposition.NoGroup)
	else
		track:solo_control():set_value(0, PBD.GroupControlDisposition.NoGroup)
	end
end

function set_active(track, state)
	track:set_active(state, nil)
end

function unsolo_all_tracks()
	Editor:select_all_tracks()
	local sel = Editor:get_selection ()
	for t in sel.tracks:routelist ():iter () do
		t:solo_control():set_value(0, PBD.GroupControlDisposition.NoGroup)
		t:mute_control():set_value(0, PBD.GroupControlDisposition.NoGroup)
	end
end


function unactive_all_tracks()
	Editor:select_all_tracks()
	local sel = Editor:get_selection ()
	for t in sel.tracks:routelist ():iter () do
		set_active(t, false)
	end
end

function delete_all_regions()
	Editor:access_action("Editor","select-all-objects")
	local sel = Editor:get_selection ()
	for r in sel.regions:regionlist ():iter () do
		if r:isnil() then goto next end
		print ("remove", r:name())
		Editor:access_action("Region","remove-region")
		::next::
	end
end

function sleep(n)
  if n > 0 then os.execute("ping -n " .. tonumber(n+1) .. " localhost > NUL") end
end

function mixdown(prefix,number,name,suffix)
	
	print("# Next file",number.."_"..name)
	fullpath = prefix .. number.."_"..name .. suffix

	if not file_exists(fullpath) then
		print("File", number.."_"..name..suffix, "does not exist. Skipping..")
		return
	end
	
	newfilestr = "C:/Users/oyster/Dropbox/WHF/ArtificialSongGenerator_workspace/Sampling/Database-ISMIR2020/AAM2020-flac-only-1000/" .. number .. "_" .. name .. ".flac"
	--os.remove(newfilestr)
	if file_exists(newfilestr) then
		print ("Flac already exists. Skipping..")
		return
	end

	local track = Session:route_by_name(name):to_track()
	--local midiTrack = Session:route_by_name("MIDI"):to_track()
	
	unsolo_all_tracks()
	--set_solo(midiTrack, true)
	if pcall(set_solo, track, true) then
		print ("Track", name, "exists and is set solo.")
	else
		print ("Track", name, "does not exist. Skipping..")
		return
	end
	
	delete_all_regions()
	
	Editor:select_all_tracks ()

	local files = C.StringVector()
	files:push_back(fullpath)

	local pos = 0
		Editor:do_import (files,
			Editing.ImportDistinctFiles, Editing.ImportToTrack, ARDOUR.SrcQuality.SrcBest,
			ARDOUR.MidiTrackNameSource.SMFTrackName,
			ARDOUR.MidiTempoMapDisposition.SMFTempoUse, -- fatal error when loading tempo, new Mixbus 6 better?
			pos, ARDOUR.PluginInfo())

	
	local locs = Session:locations ()
	local range = locs:session_range_location ()
	
	Editor:access_action("Editor","select-all-objects")
	
	local sel = Editor:get_selection ()
	for r in sel.regions:regionlist ():iter () do
		if r:isnil() then goto next end
		print ("Ranging", r:name(), "Pos:", r:position(), "Start:", r:start(), "Length", r:length())
		range:set_start(r:position(),false,false,0)
		range:set_end(r:length()+120000,false,false,0)
		::next::
	end

	-- use xdotool for windows from: https://github.com/ebranlard/xdotool-for-windows/blob/master/_dev/SwitchAndPasteToMatlab.cs
	print ("start os execute")
	local handle = io.popen("ping -n 4 localhost > NUL && C:\\bin\\xdotool.exe key \"{Enter}\"")
	print("start export")
	Editor:export_audio()
	local result = handle:read("*a")
	print (result)
	handle:close()

	print ("renaming..")
	os.rename("C:/Users/oyster/Dropbox/WHF/ArtificialSongGenerator_workspace/Sampling/Database-ISMIR2020/AAM2020-flac-only-1000/session.flac", newfilestr)
	
	print("Mixdown of " .. number.."_"..name .. " completed.")
	collectgarbage ()
end

function factory (params) return function ()

	p = "C:/Users/oyster/Dropbox/WHF/ArtificialSongGenerator_workspace/Sampling/Database-ISMIR2020/midi-only-1000/"
	s = ".mid"
	
	instrumentTable = {
		--DONE "Violin", "Viola", "Erhu", "Jinghu", "MorinKhuur",
		--DONE "Trumpet", "Flugelhorn", "Trombone", "Clarinet", "AltoSax", "TenorSax",
		--DONE "Flute", "PanFlute", "Shakuhachi", "Fujara", "Cello",
		--DONE "Piano", "BrightPiano", "ElectricPiano",
		--DONE "Ukulele", "Sitar", "Balalaika",
		--DONE "ElectricGuitarLead", "AcousticGuitar", "ElectricGuitarClean", "ElectricGuitarCrunch",
		--DONE "ElectricBass", "OrganBass", "DoubleBassPizz", "DoubleBassArco",
		--Done "Drums"
	}
	
	for k,instr in ipairs(instrumentTable) do
		for i = 1,1001,1 do
			numStr = string.format("%04.".."0".."f", i)
			mixdown(p,numStr,instr,s)
		end
	end
	
	print ("finished!")
	
	print ("prepare shutdown in 60sec..")
	os.execute("shutdown /s /f /t 60")
	
end end
