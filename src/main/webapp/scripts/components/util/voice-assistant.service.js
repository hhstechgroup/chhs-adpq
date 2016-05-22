'use strict';

angular.module('apqdApp')
    .service('VoiceAssistantService', function ($log) {

        var TIMEOUT = 900;

        var count = 0;
        var fuse = null;
        var debug = true;
        var commands = [];
        var active = false;
        var reInitRequired = false;

        function log(text) {
            if (debug) {
                $log.debug(text);
            }
        }

        function clearMessageArea() {
            $('.voice-assistant-message').children().remove();
        }

        function hideMessageArea() {
            clearMessageArea();
            $('.voice-assistant-message').addClass('hidden');
        }

        function showWhatWasSaid(command, word) {
            var message;

            if (_.isObject(command)) {
                message = command.id.replace('(*word)', '') + (!_.isUndefined(word) ? ' ' + word : '');
            } else {
                message = command;
            }

            sayInfo('<strong>You said:</strong> ' + message);
        }

        function sayInfo(messages) {
            clearMessageArea();
            var messageArea = $('.voice-assistant-message');
            messageArea.removeClass('hidden alert-danger').addClass('alert-success');

            if (_.isArray(messages)) {
                _.each(messages, function(message) {
                    messageArea.append("<span>" + message + '</span><br/>');
                });
            } else {
                messageArea.append('<span>' + messages + '</span>');
            }
        }

        function sayWarning(messages) {
            clearMessageArea();
            var messageArea = $('.voice-assistant-message');
            messageArea.removeClass('hidden alert-success').addClass('alert-danger');
            messageArea.append("<strong>Can't understand your command:</strong><br/>");

            if (_.isArray(messages)) {
                _.each(messages, function(message) {
                    messageArea.append('<span>' + message + '</span><br/>');
                });
            } else {
                messageArea.append('<span>' + messages + '</span>');
            }
        }

        function findElement(command) {
            return $('[voice_id=' + command.voice_id + ']');
        }

        function findInputByLabel(el) {
            var attrFor = $(el).attr('for');

            if (_.isUndefined(attrFor)) {
                input = $(el).next();
            } else {
                var input = $('#' + attrFor);
                if (_.isEmpty(input)) {
                    input = $(el).next();
                }
            }

            return input;
        }

        function findAngularSelectRow(el) {
            var row;
            var i = 0;
            while(!el.hasClass('ui-select-choices-row')) {
                row = el = el.parent();
                if (++i > 5) {
                    return;
                }
            }

            return row;
        }

        function findAngularSelectRoot(el) {
            var root;
            var i = 0;
            while(!el.hasClass('ui-select-container')) {
                root = el = el.parent();
                if (++i > 5) {
                    return;
                }
            }

            return root;
        }

        function clearText(text) {
            return text.trim().replace(/[-\/]/g, ' ');
        }

        function getTextFromElement(el) {
            var text = $(el).text().trim();
            if (_.isEmpty(text) || text.length < 3 || text[0] !== text[0].toUpperCase()) {
                return;
            }

            if (text.indexOf(' ') === -1 &&
                (text.indexOf('-') > -1 || text.indexOf('.') > -1) &&
                $(el).is(':hidden'))
            {
                return;
            }

            text = text.replace(/\s+/g, ' ').replace(/-/g, ' ');
            return text.toLowerCase();
        }

        function isInputTypeText(el) {
            return el.prop('tagName') === 'INPUT' &&
                  (_.isUndefined(el.attr('type')) ||
                   el.attr('type') === 'text');
        }

        function isInputTypeAngularSelect(el) {
            return el.hasClass('ui-select-choices-row-inner') ||
                   el.hasClass('ui-select-container') ||
                   el.hasClass('ui-select-focusser') ||
                   el.hasClass('ui-select-search');
        }

        function isInputTypeNativeSelect(el) {
            return el.prop('tagName') === 'SELECT';
        }

        function isInputTypeCheckbox(el) {
            return el.attr('type') === 'checkbox';
        }

        function isInputTypeTel(el) {
            return el.prop('tagName') === 'INPUT' &&
                   el.attr('type') === 'tel';
        }

        function updateInputTypeText(commandOrInput, word) {
            if (commandOrInput.voice_id) {
                var input = findElement(commandOrInput);
                input.focus();
                input.click();

                if (!_.isUndefined(word)) {
                    input.val(word.trim());
                }
            } else {
                commandOrInput.val(word.trim());
            }
        }

        function updateInputTypeTel(commandOrInput, word) {
            if (commandOrInput.voice_id) {
                var input = findElement(commandOrInput);
                input.focus();
                input.click();

                if (!_.isUndefined(word)) {
                    input.val(word.replace(/[^0-9]/g, ''));
                    input.trigger('input');
                    input.trigger('paste');
                }

            } else {
                var oldValue = commandOrInput.val().replace(/[^0-9]/g, '');
                var newValue = word.replace(/[^0-9]/g, '');
                commandOrInput.val(oldValue + newValue);
                commandOrInput.trigger('input');
                commandOrInput.trigger('paste');
            }
        }

        function updateInputTypeCheckbox(command) {
            var checkbox = findElement(command);
            checkbox.trigger('click');
        }

        function clickOnButton(command) {
            var button = findElement(command);
            button.click();
        }

        function clickOnTab(command) {
            var tab = findElement(command);

            if (tab.prop('tagName') === 'A') {
                tab.click();
            } else if (tab.parent().prop('tagName') === 'A') {
                tab.parent().click();
            }

            tab.click();
        }

        function clickOnMenu(command) {
            var menu = findElement(command);

            if (menu.prop('tagName') === 'A') {
                menu.click();
            } else if (menu.parent().prop('tagName') === 'A') {
                menu.parent().click();
            }
        }

        function clickOnAccordion(command) {
            var accordion = findElement(command);

            if (accordion.parent().prop('tagName') === 'A') {
                accordion.parent().click();
            } else if (accordion.parent().parent().prop('tagName') === 'A') {
                accordion.parent().parent().click();
            }
        }

        function clickOnAngularSelect(command) {
            var select = findElement(command);
            expandAngularSelect(select);
        }

        function clickOnNativeSelect(command) {
            var select = findElement(command);
            expandNativeSelect(select);
        }

        function clickOnAngularSelectItem(command) {
            var angularRow = findAngularSelectRow(findElement(command));
            if (!_.isUndefined(angularRow)) {
                angularRow.click();
            }
        }

        function clickOnNativeSelectItem(command) {
            var mouseClick = document.createEvent('MouseEvents');
            mouseClick.initMouseEvent("mousedown", true, true, window);

            var option = findElement(command);
            option.parent()[0].value = option.attr('value');
            option[0].dispatchEvent(mouseClick);
        }

        function scrollAngularSelect(angularSelect, down, fast) {
            var n = (fast ? 5 : 1);
            angularSelect = findAngularSelectRoot(angularSelect);

            var scrollPosition = 0;
            var currentActive = angularSelect.find('.ui-select-choices-row.active');
            var nextActive = currentActive;

            var i;
            for (i = 1; i <= n; i++) {
                var temp = down ? nextActive.next() : nextActive.prev();
                if (!_.isEmpty(temp)) {
                    scrollPosition = (down ? scrollPosition + 20 : scrollPosition - 20);
                    nextActive = temp;
                } else {
                    break;
                }
            }

            if (!_.isUndefined(nextActive)) {
                currentActive.removeClass('active');
                nextActive.addClass('active');
            }

            angularSelect.find('ul').scrollTop(angularSelect.find('ul').scrollTop() + scrollPosition);
        }

        function expandAngularSelect(angularSelect) {
            angularSelect.find('span[ng-click]').click();

            setTimeout(function() {
                _.each(angularSelect.find('a'), function(el) {

                    var text = clearText($(el).text());
                    if (!_.isEmpty(text)) {
                        var command = createCommand4Element($(el), 'ui-select-item', text);
                        commands.push(command);
                        annyang.removeCommands(_.keys(command.cmd));
                        annyang.addCommands(command.cmd);
                    }
                });

            }, TIMEOUT);
        }

        function expandNativeSelect(nativeSelect) {
            var mouseClick = document.createEvent('MouseEvents');
            mouseClick.initMouseEvent("mousedown", true, true, window);
            nativeSelect[0].dispatchEvent(mouseClick);

            _.each(nativeSelect.find('option'), function (el) {
                var text = clearText($(el).text());
                if (!_.isEmpty(text)) {
                    var command = createCommand4Element($(el), 'select-item', text);
                    commands.push(command);
                    annyang.removeCommands(_.keys(command.cmd));
                    annyang.addCommands(command.cmd);
                }
            });
        }

        function createCommand4Element(el, type, text) {
            if (_.isUndefined(text)) {
                text = getTextFromElement(el);
            }

            if (!_.isUndefined(text)) {
                var callback;
                var voice_id = count++;
                $(el).attr('voice_id', voice_id);

                if (type === 'text') {
                    text = text + ' (*word)';
                    callback = function(word) {
                        showWhatWasSaid(command, word);
                        updateInputTypeText(command, word);
                        finalizeCommand();
                    }
                } else if (type === 'tel') {
                    callback = function(word) {
                        showWhatWasSaid(command, word);
                        updateInputTypeTel(command, word);
                        finalizeCommand();
                    }
                } else if (type === 'checkbox') {
                    callback = function() {
                        showWhatWasSaid(command);
                        updateInputTypeCheckbox(command);
                        finalizeCommand();
                    }
                } else if (type === 'button') {
                    callback = function() {
                        showWhatWasSaid(command);
                        clickOnButton(command);
                        finalizeCommand();
                    }
                } else if (type === 'menu') {
                    callback = function() {
                        showWhatWasSaid(command);
                        clickOnMenu(command);
                        finalizeCommand();
                    }
                } else if (type === 'tab') {
                    callback = function() {
                        showWhatWasSaid(command);
                        clickOnTab(command);
                        finalizeCommand();
                    }
                } else if (type === 'accordion') {
                    callback = function() {
                        showWhatWasSaid(command);
                        clickOnAccordion(command);
                        finalizeCommand();
                    }
                } else if (type === 'ui-select') {
                    callback = function() {
                        showWhatWasSaid(command);
                        clickOnAngularSelect(command);
                        reInitRequired = true;
                    }
                } else if (type === 'ui-select-item') {
                    callback = function() {
                        showWhatWasSaid(command);
                        clickOnAngularSelectItem(command);
                        finalizeCommand();
                    }
                } else if (type === 'select') {
                    callback = function() {
                        showWhatWasSaid(command);
                        clickOnNativeSelect(command);
                        reInitRequired = true;
                    }
                } else if (type === 'select-item') {
                    callback = function() {
                        showWhatWasSaid(command);
                        clickOnNativeSelectItem(command);
                        finalizeCommand();
                    }
                }

                var command = {id: text, voice_id: voice_id, cmd: {}, type: type};
                command.cmd[text] = callback;
                return command;
            }
        }

        function createCommands4Buttons() {
            var commands = [];

            _.each($('button'), function (el) {
                var command = createCommand4Element(el, 'button');
                if (!_.isUndefined(command)) {
                    commands.push(command);
                }
            });

            return commands;
        }

        function createCommands4Tabs() {
            var commands = [];

            _.each($('ul.nav a[ng-click]'), function (el) {
                var command = createCommand4Element(el, 'tab');
                if (!_.isUndefined(command)) {
                    commands.push(command);
                }
            });

            return commands;
        }

        function createCommands4Menu() {
            var commands = [];

            _.each($("a[ui-sref] > span:not(:empty)"), function (el) {
                var command = createCommand4Element(el, 'menu');
                if (!_.isUndefined(command)) {
                    commands.push(command);
                }
            });

            return commands;
        }

        function createCommands4Accordions() {
            var commands = [];

            _.each($('a.accordion-toggle span.ng-scope'), function (el) {
                var command = createCommand4Element(el, 'accordion');
                if (!_.isUndefined(command)) {
                    commands.push(command);
                }
            });

            return commands;
        }

        function createCommands4Labels() {
            var commands = [];

            _.each($('label'), function (el) {
                var command = null;

                var input = findInputByLabel(el);

                if (isInputTypeText(input)) {
                    command = createCommand4Element(input, 'text', getTextFromElement(el));

                } else if (isInputTypeTel(input)) {
                    command = createCommand4Element(input, 'tel', getTextFromElement(el));

                } else if (isInputTypeCheckbox(input)) {
                    command = createCommand4Element(input, 'checkbox', getTextFromElement(el));

                } else if (isInputTypeAngularSelect(input)) {
                    command = createCommand4Element(input, 'ui-select', getTextFromElement(el));

                } else if (isInputTypeNativeSelect(input)) {
                    command = createCommand4Element(input, 'select', getTextFromElement(el));
                }

                if (!_.isNil(command)) {
                    commands.push(command);
                }
            });

            return commands;
        }

        function createCommands() {
            return [].concat(createCommands4Accordions()).
                      concat(createCommands4Buttons()).
                      concat(createCommands4Labels()).
                      concat(createCommands4Tabs()).
                      concat(createCommands4Menu());
        }

        function finalizeCommand() {
            if (reInitRequired) {
                reInitAnnyang();
                reInitRequired = false;
            }
        }

        function performCommandShowMeCommands() {
            showWhatWasSaid('show me commands');
            _.each(commands, function(command) {
                log("command: '" + command.id + "'; type: " + command.type);
            });
        }

        function performCommandClear() {
            showWhatWasSaid('delete');
            var input = $(document.activeElement);

            if (isInputTypeText(input)) {
                updateInputTypeText(input, '');

            } else if (isInputTypeTel(input)) {
                updateInputTypeTel(input, '');
            }
        }

        function performCommandNext() {
            showWhatWasSaid('next');
            $.emulateTab();

            //var input = $(document.activeElement);
            //if (isInputTypeAngularSelect(input)) {
            //    expandAngularSelect(input);
            //} else if (isInputTypeNativeSelect(input)) {
            //    expandNativeSelect(input);
            //}
            //    log('moveFocus ' + forward);
            //
            //    if (forward) {
            //        $.emulateTab();
            //    } else {
            //        $.emulateTab(-1);
            //    }
            //
            //    if ($(document.activeElement).hasClass('ui-select-focusser')) {
            //
            //        var selected = $(document.activeElement);
            //        while(!selected.hasClass('ui-select-container')) {
            //            selected = selected.parent();
            //        }
            //
            //        uiSelectInit(selected.attr('id'));
            //
            //    } else if (_.isUndefined($(document.activeElement).attr('type')) ||
            //         $(document.activeElement).attr('type') === 'text') {
            //        focus = $(document.activeElement);
            //    } else {
            //        focus = null;
            //    }
        }

        function performCommandPrevious() {
            showWhatWasSaid('previous');
            $.emulateTab(-1);

            //var input = $(document.activeElement);
            //if (isInputTypeAngularSelect(input)) {
            //    expandAngularSelect(input);
            //} else if (isInputTypeNativeSelect(input)) {
            //    expandNativeSelect(input);
            //}
        }

        function performCommandScrollUp() {
            showWhatWasSaid('scroll up');
            var input = $(document.activeElement);
            if (isInputTypeAngularSelect(input)) {
                scrollAngularSelect(input, false, true);
                return;
            }

            var body = $('body');
            var step = (($(document).height() - $(window).height())/3).toFixed(0);
            body.scrollTop(body.scrollTop() - step);
        }

        function performCommandScrollDown() {
            showWhatWasSaid('scroll down');
            var input = $(document.activeElement);
            if (isInputTypeAngularSelect(input)) {
                scrollAngularSelect(input, true, true);
                return;
            }

            var body = $('body');
            var step = (($(document).height() - $(window).height())/3).toFixed(0);
            body.scrollTop(body.scrollTop() + step);
        }

        function performCommandUp() {
            showWhatWasSaid('up');
            var input = $(document.activeElement);
            if (isInputTypeAngularSelect(input)) {
                scrollAngularSelect(input, false, false);
            }
        }

        function performCommandDown() {
            showWhatWasSaid('down');
            var input = $(document.activeElement);
            if (isInputTypeAngularSelect(input)) {
                scrollAngularSelect(input, true, false);
            }
        }

        function performCommandSelect() {
            showWhatWasSaid('select');
            //    if (select) {
            //        $('#' + uiSelectId).find('.ui-select-choices-row.active').click();
            //    } else {
            //        $('#' + uiSelectId).click();
            //    }
            //}
        }

        function performCommandCancel() {
            showWhatWasSaid('cancel');
        }

        function performNoMatch(variants) {

            var input = $(document.activeElement);
            if (isInputTypeText(input) && _.isEmpty(input.val())) {
                showWhatWasSaid(variants[0]);
                updateInputTypeText(input, variants[0]);
                performCommandNext();

            } else if (isInputTypeTel(input)) {
                showWhatWasSaid(variants[0]);
                updateInputTypeTel(input, variants[0]);

            } else {
                sayWarning(variants);
                performUsingFuse(variants);

            }
        }

        function performUsingFuse(text) {

            //} else if (_.isNil(focus)) {
            //    log('Annyang No Match with 1: ' + variants);
            //    reInitAnnyang(true);
            //    var r1 = performUsingFuse(variants[0]);
            //    var r2 = performUsingFuse(variants[1]);
            //
            //    if (r1 === r2 && !_.isUndefined(r1)) {
            //        doCommand(r1);
            //    } else if (!_.isUndefined(r1) && _.isUndefined(r2)) {
            //        doCommand(r1);
            //    } else if (_.isUndefined(r1) && !_.isUndefined(r2)) {
            //        doCommand(r2);
            //    }
            //} else {
            //    log('Annyang No Match with 2: ' + variants);
            //}
            //
            //if (!_.isNil(fuse) || !_.isUndefined(text)) {
            //    var results = fuse.search(text);
            //    _.each(results, function(i) {
            //        log('Fuse Match with: ' + i.id);
            //    });
            //
            //    if (results.length === 1) {
            //        return results[0];
            //    } else if (results.length === 0) {
            //        log('Fuse No Match with: ' + text);
            //    }
            //}
        }

        function initNarrative() {
            sayInfo('Narrative is active');
            $('#editor').find('textarea').focus().click();
            annyang.removeCommands();
            annyang.addCommands({'*any': function(newValue) {

                var fuse = initFuse(['stop narrative', 'delete']);
                var fuseResult = fuse.search(newValue.trim());
                if (!_.isEmpty(fuseResult)) {
                    if (fuseResult[0] === 0) {
                        $.emulateTab(-1);
                        sayInfo('Narrative is deactivated');
                        reInitAnnyang();
                        return;
                    }

                    if (fuseResult[0] === 1) {
                        ace.edit('editor').setValue(removeLastWord(ace.edit('editor').getValue()));
                        return;
                    }
                }

                var oldValue = ace.edit('editor').getValue();
                if (isSpaceRequired(oldValue, newValue)) {
                    newValue = ' ' + newValue;
                }

                ace.edit('editor').setValue(oldValue + newValue);
            }});
        }

        function isSpaceRequired(oldValue, newValue) {
            if (!_.isEmpty(oldValue) && !oldValue.endsWith(' ') && !newValue.startsWith(' ') &&
                !_.isUndefined(oldValue[oldValue.length - 1].match(/[^0-9]/)))
            {
                return true;
            }
        }

        function removeLastWord(str) {
            var lastIndex = str.lastIndexOf(" ");
            return str.substring(0, lastIndex);
        }

        function initFuse(commands) {
            return new Fuse(commands, {
                maxPatternLength: 32,
                caseSensitive: false,
                includeScore: false,
                shouldSort: true,
                tokenize: false,
                threshold: 0.2,
                distance: 5,
                location: 0
            })
        }

        function reInitAnnyang() {
            commands = createCommands();

            annyang.removeCommands();
            annyang.addCommands({'show me commands': performCommandShowMeCommands});
            annyang.addCommands({'narrative': initNarrative});
            annyang.addCommands({'scroll down': performCommandScrollDown});
            annyang.addCommands({'scroll up': performCommandScrollUp});
            annyang.addCommands({'previous': performCommandPrevious});
            annyang.addCommands({'select': performCommandSelect});
            annyang.addCommands({'delete': performCommandClear});
            annyang.addCommands({'next': performCommandNext});
            annyang.addCommands({'down': performCommandDown});
            annyang.addCommands({'up': performCommandUp});

            _.each(commands, function(command) {
                annyang.addCommands(command.cmd);
            });

            if (!annyang.isListening()) {
                annyang.addCallback('resultNoMatch', function (variants) {
                    performNoMatch(variants);
                });

                annyang.start({continuous: false});
                active = true;
            }
        }


        function triggerVoiceAssistant() {
            if (!active && annyang.isListening()) {
                return;
            }

            if (annyang.isListening()) {
                hideMessageArea();
                annyang.abort();
                active = false;
            } else {
                reInitAnnyang();
                sayInfo('Hi, there!');
            }
        }

        function initVoiceAssistance() {
            if (active) {
                setTimeout(reInitAnnyang, TIMEOUT);
            }
        }

        function isVoiceAssistantActive() {
            return active;
        }

        return {
            triggerVoiceAssistant: triggerVoiceAssistant,
            isVoiceAssistantActive: isVoiceAssistantActive,
            initVoiceAssistance: initVoiceAssistance
        }
    });
