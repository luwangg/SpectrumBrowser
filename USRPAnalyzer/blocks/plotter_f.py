#!/usr/bin/env python

# USRPAnalyzer - spectrum sweep functionality for USRP and GNURadio
# Copyright (C) Douglas Anderson
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.


from __future__ import division

import numpy as np

from gnuradio import gr


class plotter_f(gr.sync_block):
    def __init__(self, plot_iface, plot_vec_len, max_plotted_bin):
        # Formula for scaling from num of total bins to num of valid bins
        self.n_in = plot_vec_len
        self.max_bin = max_plotted_bin # crop plotted data to requested span

        gr.sync_block.__init__(
            self,
            name="plotter_f",
            in_sig=[(np.float32, self.n_in)],
            out_sig=None
        )

        self.plot_iface = plot_iface
        self.plot_iface.redraw_plot.set()

    def work(self, input_items, output_items):
        in0 = input_items[0]
        ninput_items = len(in0)
        assert(len(in0[0]) == self.n_in)

        if self.plot_iface.gui_idle.is_set():
            gui_alive = self.plot_iface.update(in0[0][:self.max_bin])
            if not gui_alive:
                return -1

        return ninput_items
