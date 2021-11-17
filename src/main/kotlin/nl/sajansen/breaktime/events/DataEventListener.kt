package nl.sajansen.breaktime.events


interface DataEventListener {
    fun onActionsUpdated() {}
    fun onRunningStateUpdated() {}
}