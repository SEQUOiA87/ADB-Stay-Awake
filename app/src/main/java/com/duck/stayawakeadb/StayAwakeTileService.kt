package com.duck.stayawakeadb

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class StayAwakeTileService : TileService() {

    override fun onClick() {
        super.onClick()

        HelperUtil.globallyEnabled = !HelperUtil.globallyEnabled

        updateTile()
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
    }

    override fun onTileAdded() {
        super.onTileAdded()
    }

    override fun onStartListening() {
        super.onStartListening()

        updateTile()
    }

    private fun updateTile() {
        val tile = qsTile

        //        tile.icon = Icon.createWithResource(this, R.drawable.ic_tile_service)
        //        tile.label = getString(R.string.app_name)
        tile.state = if (HelperUtil.globallyEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE

        tile.updateTile()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onStopListening() {
        super.onStopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}