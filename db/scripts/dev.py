def create_matches(src_names, dst_names, hint_map={}):
    src_names = src_names[:]
    dst_names = dst_names[:]
    map = {}
    for k in hint_map.keys():
        map[k] = hint_map[k]
        src_names.remove(k)
        dst_names.remove(hint_map[k])
    for k in src_names:
        if k in dst_names:
            map[k] = k
            src_names.remove(k)
            dst_names.remove(k)
    handled = 1
    while handled > 0:
        handled = 0
        for s in src_names:
            for t in dst_names:
                if (s in t) or (t in s) :
                    map[s] = t
                    src_names.remove(s)
                    dst_names.remove(t)
                    handled += 1
    if len(src_names) > 0:
        print(src_names)
        raise RuntimeError("Cannot match names.")
    return map
