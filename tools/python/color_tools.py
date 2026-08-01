import random
from typing import ClassVar

class Color:
    def __init__(self, r: int | str, g: int | str | None = None, b: int | str | None = None):
        if isinstance(r, str) and g is None and b is None:
            hex_string = r.replace("#", "")
            self.r = int(hex_string[0:2], 16)
            self.g = int(hex_string[2:4], 16)
            self.b = int(hex_string[4:6], 16)
        else:
            self.r = int(r, 16) if type(r) is str else r
            self.g = int(g, 16) if type(g) is str else g
            self.b = int(b, 16) if type(b) is str else b

    def get_hue(self) -> float:
        max_sat = max(self.r, self.g, self.b) / 255
        min_sat = min(self.r, self.g, self.b) / 255
        chroma = max_sat - min_sat
        r_decimal = self.r / 255
        g_decimal = self.g / 255
        b_decimal = self.b / 255
        if self.r > self.g >= self.b or self.r == self.g > self.b: # R is most saturated, or R and G are most saturated
            hue = 60 * ((g_decimal - b_decimal) / chroma % 6)
        elif self.g > self.b >= self.r or self.g == self.b > self.r: # G is most saturated, or G and B are most saturated
            hue = 60 * ((b_decimal - r_decimal) / chroma + 2)
        elif self.b > self.g >= self.r or self.b == self.r > self.g: # B is most saturated, or R and B are most saturated
            hue = 60 * ((r_decimal - g_decimal) / chroma + 4)
        else: # No color is most saturated (the color is a shade)
            hue = 0 # Hue is undefined, but 0 by standard practice
        return hue / 360

    def get_saturation(self) -> float:
        max_sat = max(self.r, self.g, self.b)
        min_sat = min(self.r, self.g, self.b)
        chroma = max_sat - min_sat
        saturation = 0
        if max_sat != 0:
            saturation = chroma / max_sat
        return saturation

    def get_value(self) -> float:
        return max(self.r, self.g, self.b) / 255

    def rgb(self) -> tuple[int, int, int]:
        return self.r, self.g, self.b

    def hsv(self) -> tuple[float, float, float]:
        return self.get_hue(), self.get_saturation(), self.get_value()

    def cmyk(self) -> tuple[float, float, float, float]:
        r_decimal = self.r / 255
        g_decimal = self.g / 255
        b_decimal = self.b / 255
        k = 1 - max(r_decimal, g_decimal, b_decimal)
        if k == 1:
            return 0, 0, 0, 1
        c = (1 - r_decimal - k) / (1 - k)
        m = (1 - g_decimal - k) / (1 - k)
        y = (1 - b_decimal - k) / (1 - k)
        return c, m, y, k

    def merge(self, other: 'Color', alpha: float = 0.5) -> 'Color':
        """ Merges two colors analogous to overlaying other color with the given alpha (default 50%) on top of self color. """
        alpha255 = min(max(alpha * 255, 0), 255)
        channel_result = lambda i: int((alpha255 * self[i] + (255 - alpha255) * other[i]) // 255)
        return Color(channel_result(0), channel_result(1), channel_result(2))

    def __getitem__(self, index):
        return [self.r, self.g, self.b][index]

    def __setitem__(self, index, value: int | str):
        if index == 0:
            self.r = int(value, 16) if type(value) is str else value
        elif index == 1:
            self.g = int(value, 16) if type(value) is str else value
        elif index == 2:
            self.b = int(value, 16) if type(value) is str else value
        else: raise IndexError(f'index {index} is out of range for Color')

    def __str__(self, hsv: bool = False):
        if hsv:
            return f'hsv({self.get_hue()}, {self.get_saturation()}, {self.get_value()})'
        return f'#{hex(self.r)[2:].rjust(2,'0')}{hex(self.g)[2:].rjust(2,'0')}{hex(self.b)[2:].rjust(2,'0')}'

    def __eq__(self, other):
        return self.r == other.r and self.g == other.g and self.b == other.b

    def __lt__(self, other):
        return self.get_saturation() < other.get_saturation()

    def __gt__(self, other):
        return self.get_saturation() > other.get_saturation()

    def closeness(self, other) -> float:
        r_closeness = 1 - (self.r - other.r) / 256
        g_closeness = 1 - (self.g - other.g) / 256
        b_closeness = 1 - (self.b - other.b) / 256
        return ((r_closeness + g_closeness + b_closeness) / 3) ** 2

    def __hash__(self):
        return hash((self.r, self.g, self.b))

    RED: ClassVar['Color']
    YELLOW: ClassVar['Color']
    GREEN: ClassVar['Color']
    CYAN: ClassVar['Color']
    BLUE: ClassVar['Color']
    MAGENTA: ClassVar['Color']
    WHITE: ClassVar['Color']
    BLACK: ClassVar['Color']

Color.RED     = Color(255, 0,   0)
Color.YELLOW  = Color(255, 255, 0)
Color.GREEN   = Color(0,   0,   255)
Color.CYAN    = Color(0,   255, 255)
Color.BLUE    = Color(0,   0,   255)
Color.MAGENTA = Color(255, 0,   255)
Color.WHITE   = Color(255, 255, 255)
Color.BLACK   = Color(0,   0,   0)

def random_color() -> Color:
    r = random.randint(0, 255)
    g = random.randint(0, 255)
    b = random.randint(0, 255)
    return Color(r, g, b)

